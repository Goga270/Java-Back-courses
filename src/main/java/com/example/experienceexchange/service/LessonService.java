package com.example.experienceexchange.service;

import com.example.experienceexchange.dto.LessonDto;
import com.example.experienceexchange.dto.PaymentDto;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Map;

@Service
public class LessonService implements ILessonService {

    private static final String SUB_TO_OWN_LESSON = "Subscription to own lesson is not possible";
    private static final String SUB_TO_CLOSE_LESSON = "Lesson is closed for subscription";
    private static final String SUB_WITH_INCORRECT_PRICE = "Entered price is less than the fixed price";
    private static final String NOT_ACCESS_EDIT = "No access to edit resource";
    private static final String NOT_FOUND_LESSON_IN_SUBSCRIPTIONS = "lesson not found in subscriptions";
    private static final String LESSON_NOT_START = "lesson has not started";

    private final ILessonRepository lessonRepository;
    private final IUserRepository userRepository;
    private final IFilterProvider filterProvider;
    private final LessonMapper lessonMapper;
    private final PaymentMapper paymentMapper;
    private final IPaymentRepository paymentRepository;

    public LessonService(ILessonRepository lessonRepository,
                         IUserRepository userRepository,
                         @Qualifier("lessonFilter") IFilterProvider jpqlFilterProvider,
                         LessonMapper lessonMapper,
                         PaymentMapper paymentMapper,
                         IPaymentRepository paymentRepository) {
        this.lessonRepository = lessonRepository;
        this.userRepository = userRepository;
        this.filterProvider = jpqlFilterProvider;
        this.lessonMapper = lessonMapper;
        this.paymentMapper = paymentMapper;
        this.paymentRepository = paymentRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<LessonDto> getLessons(List<SearchCriteria> filters) {
        Map<String, List<SearchCriteria>> searchMap = filterProvider.createSearchMap(filters);
        String filterQuery = filterProvider.createFilterQuery(searchMap);

        List<LessonSingle> lessons = lessonRepository.findAllLessonsByFilter(filterQuery);
        return lessonMapper.toLessonDto(lessons);
    }

    // TODO: НАДО КАК ТО ОТПИСЫВАТЬ ПОЛЬЗОВАТЕЛЕЙ КОГДА ВРЕМЯ КУРСА КОНЧАЕТСЯ
    @Transactional(readOnly = true)
    @Override
    public LessonDto getLesson(Long lessonId) {
        LessonSingle lesson = getLessonById(lessonId);
        return lessonMapper.lessonToLessonDto(lesson);
    }

    @Transactional(readOnly = true)
    @Override
    public LessonDto getLesson(JwtUserDetails userDetails, Long lessonId) {
        List<LessonSingle> lessons = lessonRepository.findAllLessonsByUserId(userDetails.getId());
        LessonSingle lessonSingle = lessons.stream()
                .filter(lesson -> lesson.getId().equals(lessonId))
                .findFirst()
                .orElse(null);

        if (lessonSingle == null) {
            throw new NotAccessException(NOT_FOUND_LESSON_IN_SUBSCRIPTIONS);
        }

        if (DateUtil.isDateBeforeNow(lessonSingle.getStartLesson())) {
            throw new NotAccessException(LESSON_NOT_START);
        }
        return lessonMapper.lessonToLessonDto(lessonSingle);
    }

    @Transactional(readOnly = true)
    @Override
    public List<LessonDto> getSchedule(JwtUserDetails userDetails) {
        Long userId = userDetails.getId();
        List<LessonSingle> allLessons = lessonRepository.findAllLessonsByUserId(userId);
        return lessonMapper.toLessonDto(allLessons);
    }

    @Transactional
    @Override
    public LessonDto createLesson(JwtUserDetails userDetails, LessonDto lessonDto) {
        Long userId = userDetails.getId();
        User author = userRepository.find(userId);

        LessonSingle newLesson = lessonMapper.lessonDtoToLesson(lessonDto);
        newLesson.setAuthor(author);
        // TODO : TEST
        lessonRepository.save(newLesson);
        LessonSingle update = lessonRepository.update(newLesson);
        return lessonMapper.lessonToLessonDto(update);

    }

    @Transactional
    @Override
    public LessonDto editLesson(JwtUserDetails userDetails, Long lessonId, LessonDto lessonDto) {
        LessonSingle lessonBeforeUpdate = getLessonById(lessonId);

        checkAccessToLessonEdit(lessonBeforeUpdate.getAuthor().getId(), userDetails.getId());

        LessonSingle lessonAfterUpdate = lessonMapper.lessonDtoToLesson(lessonDto);
        updateLesson(lessonBeforeUpdate, lessonAfterUpdate);
        // TODO : TEST
        LessonSingle updateLesson = lessonRepository.update(lessonAfterUpdate);
        return lessonMapper.lessonToLessonDto(updateLesson);
    }

    @Transactional
    @Override
    public void deleteLesson(JwtUserDetails userDetails, Long id) {
        try {
            lessonRepository.deleteById(id);
        } catch (EntityExistsException e) {
            throw new LessonNotFoundException(id);
        }
    }

    @Transactional
    @Override
    public PaymentDto subscribeToLesson(JwtUserDetails userDetails, PaymentDto paymentDto, Long lessonId) {
        LessonSingle lesson = getLessonById(lessonId);
        User user = userRepository.find(userDetails.getId());

        if (lesson.getAuthor().getId().equals(user.getId())) {
            throw new SubscriptionNotPossibleException(SUB_TO_OWN_LESSON);
        }

        if (lesson.isSatisfactoryPrice(paymentDto.getPrice())) {
            if (lesson.isAvailableForSubscription(DateUtil.dateTimeNow())) {
                Payment payment = subscribeToLesson(paymentDto, user, lesson);
                paymentRepository.save(payment);
                return paymentMapper.paymentToPaymentDto(payment);
            } else {
                throw new SubscriptionNotPossibleException(SUB_TO_CLOSE_LESSON);
            }
        } else {
            throw new SubscriptionNotPossibleException(SUB_WITH_INCORRECT_PRICE);
        }
    }

    private LessonSingle getLessonById(Long lessonId) throws LessonNotFoundException {
        LessonSingle lesson = lessonRepository.find(lessonId);
        if (lesson == null) {
            throw new LessonNotFoundException(lessonId);
        }
        return lesson;
    }

    private void checkAccessToLessonEdit(Long authorId, Long authUserID) throws NotAccessException {
        if (!authorId.equals(authUserID)) {
            throw new NotAccessException(NOT_ACCESS_EDIT);
        }
    }

    private LessonSingle updateLesson(LessonSingle lessonBeforeUpdate, LessonSingle lessonAfterUpdate) {
        lessonAfterUpdate.setAuthor(lessonBeforeUpdate.getAuthor());
        lessonAfterUpdate.setCurrentNumberUsers(lessonBeforeUpdate.getCurrentNumberUsers());
        lessonAfterUpdate.setComments(lessonBeforeUpdate.getComments());
        lessonAfterUpdate.setUsersInLesson(lessonBeforeUpdate.getUsersInLesson());
        return lessonAfterUpdate;
    }

    private Payment subscribeToLesson(PaymentDto paymentDto, User user, LessonSingle lesson) {
        Payment payment = paymentMapper.paymentDtoToPayment(paymentDto);
        payment.setDatePayment(DateUtil.dateTimeNow());
        user.addLesson(lesson);
        user.addPayment(payment);
        payment.setLesson(lesson);
        payment.setCostumer(user);
        lesson.increaseNumberSubscriptions();
        return payment;
    }
}

