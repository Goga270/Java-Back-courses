package com.example.experienceexchange.service;

import com.example.experienceexchange.dto.LessonDto;
import com.example.experienceexchange.dto.PaymentDto;
import com.example.experienceexchange.exception.IllegalSearchCriteriaException;
import com.example.experienceexchange.exception.LessonNotFoundException;
import com.example.experienceexchange.exception.NotAccessException;
import com.example.experienceexchange.exception.SubscriptionNotPossibleException;
import com.example.experienceexchange.model.LessonSingle;
import com.example.experienceexchange.model.Payment;
import com.example.experienceexchange.model.User;
import com.example.experienceexchange.repository.filter.IFilterProvider;
import com.example.experienceexchange.repository.filter.SearchCriteria;
import com.example.experienceexchange.repository.interfaceRepo.ILessonRepository;
import com.example.experienceexchange.repository.interfaceRepo.IPaymentRepository;
import com.example.experienceexchange.repository.interfaceRepo.IUserRepository;
import com.example.experienceexchange.security.JwtUserDetails;
import com.example.experienceexchange.service.interfaceService.ILessonService;
import com.example.experienceexchange.util.date.DateUtil;
import com.example.experienceexchange.util.mapper.LessonMapper;
import com.example.experienceexchange.util.mapper.PaymentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class LessonService implements ILessonService {

    public static final String SUB_TO_OWN_LESSON = "Subscription to own lesson is not possible";
    public static final String SUB_TO_CLOSE_LESSON = "Lesson is closed for subscription";
    public static final String SUB_WITH_INCORRECT_PRICE = "Entered price is less than the fixed price";
    public static final String NOT_ACCESS_EDIT = "No access to edit resource";
    public static final String NOT_FOUND_LESSON_IN_SUBSCRIPTIONS = "lesson not found in subscriptions";
    public static final String LESSON_NOT_START = "lesson has not started";
    public static final String ILLEGAL_FILTER = "Entered search criteria is incorrect";
    public static final String LESSON_NOT_END = "Lesson is not ended";


    private final ILessonRepository lessonRepository;
    private final IUserRepository userRepository;
    private final IPaymentRepository paymentRepository;
    private final IFilterProvider filterProvider;
    private final LessonMapper lessonMapper;
    private final PaymentMapper paymentMapper;
    private final DateUtil dateUtil;

    public LessonService(ILessonRepository lessonRepository,
                         IUserRepository userRepository,
                         @Qualifier("lessonFilter") IFilterProvider jpqlFilterProvider,
                         LessonMapper lessonMapper,
                         PaymentMapper paymentMapper,
                         IPaymentRepository paymentRepository,
                         DateUtil dateUtil) {
        this.lessonRepository = lessonRepository;
        this.userRepository = userRepository;
        this.filterProvider = jpqlFilterProvider;
        this.lessonMapper = lessonMapper;
        this.paymentMapper = paymentMapper;
        this.paymentRepository = paymentRepository;
        this.dateUtil = dateUtil;
    }

    @Transactional(readOnly = true)
    @Override
    public List<LessonDto> getLessons(List<SearchCriteria> filters) {
        log.debug("Get lessons");
        Map<String, List<SearchCriteria>> searchMap = filterProvider.createSearchMap(filters);
        String filterQuery = filterProvider.createFilterQuery(searchMap);
        try {
            List<LessonSingle> lessons = lessonRepository.findAllLessonsByFilter(filterQuery);
            return lessonMapper.toLessonDto(lessons);
        } catch (InvalidDataAccessResourceUsageException e) {
            log.warn("Incorrect filter {} for query", filterQuery);
            throw new IllegalSearchCriteriaException(ILLEGAL_FILTER);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public LessonDto getLesson(Long lessonId) {
        log.debug("Get lesson {}", lessonId);
        LessonSingle lesson = getLessonById(lessonId);
        return lessonMapper.lessonToLessonDto(lesson);
    }

    @Transactional(readOnly = true)
    @Override
    public LessonDto getLesson(JwtUserDetails userDetails, Long lessonId) {
        log.debug("Get lesson {} for subscriber", lessonId);

        LessonSingle lessonSingle = lessonRepository.findLessonSingleByUserIdAndLessonId(userDetails.getId(), lessonId);

        if (lessonSingle == null) {
            log.warn("No access to lesson {} for {}", lessonId, userDetails.getId());
            throw new NotAccessException(NOT_FOUND_LESSON_IN_SUBSCRIPTIONS);
        }

        if (dateUtil.isDateAfterNow(lessonSingle.getStartLesson())) {
            log.warn("No access to unstarted lesson {}", lessonId);
            throw new NotAccessException(LESSON_NOT_START);
        }
        return lessonMapper.lessonToLessonDto(lessonSingle);
    }

    @Transactional(readOnly = true)
    @Override
    public List<LessonDto> getSchedule(JwtUserDetails userDetails) {
        log.debug("Get schedule of lessons for user {}", userDetails.getId());
        Long userId = userDetails.getId();
        List<LessonSingle> allLessons = lessonRepository.findAllLessonsByUserId(userId);
        return lessonMapper.toLessonDto(allLessons);
    }

    @Transactional
    @Override
    public LessonDto createLesson(JwtUserDetails userDetails, LessonDto lessonDto) {
        log.debug("Create lesson");
        Long userId = userDetails.getId();
        User author = userRepository.find(userId);

        LessonSingle newLesson = lessonMapper.lessonDtoToLesson(lessonDto);
        newLesson.setAuthor(author);

        lessonRepository.save(newLesson);
        lessonRepository.update(newLesson);
        log.debug("Created lesson {}", newLesson.getId());

        return lessonMapper.lessonToLessonDto(newLesson);
    }

    @Transactional
    @Override
    public LessonDto restartLesson(JwtUserDetails userDetails, LessonDto lessonDto) {
        log.debug("Restart lesson");
        User author = userRepository.find(userDetails.getId());
        LessonSingle lesson = getLessonById(lessonDto.getId());

        checkAccessToLessonEdit(lesson.getAuthor().getId(), author.getId());

        if (dateUtil.isDateAfterNow(lesson.getEndLesson())) {
            log.warn("Lesson not restarted because it hasn't finished");
            throw new NotAccessException(LESSON_NOT_END);
        }
        LessonSingle restartedLesson = lessonMapper.lessonDtoToLesson(lessonDto);
        restartedLesson.setAuthor(author);
        restartedLesson.setCurrentNumberUsers(0);
        restartedLesson.setComments(lesson.getComments());

        LessonSingle update = lessonRepository.update(restartedLesson);
        log.debug("Restarted lesson {}", lessonDto.getId());
        return lessonMapper.lessonToLessonDto(update);
    }

    @Transactional
    @Override
    public LessonDto editLesson(JwtUserDetails userDetails, Long lessonId, LessonDto lessonDto) {
        log.debug("Edit lesson {}", lessonId);
        LessonSingle lessonBeforeUpdate = getLessonById(lessonId);

        checkAccessToLessonEdit(lessonBeforeUpdate.getAuthor().getId(), userDetails.getId());

        LessonSingle lessonAfterUpdate = lessonMapper.lessonDtoToLesson(lessonDto);
        updateLesson(lessonBeforeUpdate, lessonAfterUpdate);

        LessonSingle updateLesson = lessonRepository.update(lessonAfterUpdate);
        log.debug("Updated lesson {}", lessonId);
        return lessonMapper.lessonToLessonDto(updateLesson);
    }

    @Transactional
    @Override
    public void deleteLesson(JwtUserDetails userDetails, Long id) {
        log.debug("Delete lesson {}", id);
        LessonSingle lesson = getLessonById(id);
        checkAccessToLessonEdit(lesson.getAuthor().getId(), userDetails.getId());
        lessonRepository.delete(lesson);
        log.debug("Lesson {} deleted", id);
    }

    @Transactional
    @Override
    public PaymentDto subscribeToLesson(JwtUserDetails userDetails, PaymentDto paymentDto, Long lessonId) {
        log.debug("User {} tries to subscribe to lesson {} ", userDetails.getId(), lessonId);
        LessonSingle lesson = getLessonById(lessonId);
        User user = userRepository.find(userDetails.getId());

        if (lesson.getAuthor().getId().equals(user.getId())) {
            log.warn("User {} cannot subscribe to own lesson", userDetails.getId());
            throw new SubscriptionNotPossibleException(SUB_TO_OWN_LESSON);
        }

        if (lesson.isSatisfactoryPrice(paymentDto.getPrice())) {
            if (lesson.isAvailableForSubscription(dateUtil.dateTimeNow())) {
                Payment payment = subscribeToLesson(paymentDto, user, lesson);
                paymentRepository.save(payment);
                log.debug("User {} subscribe to lesson {}", userDetails.getId(), lessonId);
                return paymentMapper.paymentToPaymentDto(payment);
            } else {
                log.warn("User {} cannot subscribe to close lesson", userDetails.getId());
                throw new SubscriptionNotPossibleException(SUB_TO_CLOSE_LESSON);
            }
        } else {
            log.warn("User {} cannot subscribe to close lesson with incorrect price {}", userDetails.getId(), paymentDto.getPrice());
            throw new SubscriptionNotPossibleException(SUB_WITH_INCORRECT_PRICE);
        }
    }

    private LessonSingle getLessonById(Long lessonId) throws LessonNotFoundException {
        LessonSingle lesson = lessonRepository.find(lessonId);
        if (lesson == null) {
            log.warn("Lesson {} not found", lessonId);
            throw new LessonNotFoundException(lessonId);
        }
        return lesson;
    }

    private void checkAccessToLessonEdit(Long authorId, Long authUserID) throws NotAccessException {
        if (!authorId.equals(authUserID)) {
            log.warn("User {} does not have access to edit lesson", authUserID);
            throw new NotAccessException(NOT_ACCESS_EDIT);
        }
    }

    private LessonSingle updateLesson(LessonSingle lessonBeforeUpdate, LessonSingle lessonAfterUpdate) {
        log.trace("Updating lesson {}", lessonBeforeUpdate.getId());
        lessonAfterUpdate.setAuthor(lessonBeforeUpdate.getAuthor());
        lessonAfterUpdate.setCurrentNumberUsers(lessonBeforeUpdate.getCurrentNumberUsers());
        lessonAfterUpdate.setComments(lessonBeforeUpdate.getComments());
        lessonAfterUpdate.setUsersInLesson(lessonBeforeUpdate.getUsersInLesson());
        return lessonAfterUpdate;
    }

    private Payment subscribeToLesson(PaymentDto paymentDto, User user, LessonSingle lesson) {
        Payment payment = paymentMapper.paymentDtoToPayment(paymentDto);
        payment.setDatePayment(dateUtil.dateTimeNow());
        user.addLesson(lesson);
        user.addPayment(payment);
        payment.setLesson(lesson);
        payment.setCostumer(user);
        lesson.increaseNumberSubscriptions();
        return payment;
    }
}

