package com.example.experienceexchange.service;

import com.example.experienceexchange.dto.LessonDto;
import com.example.experienceexchange.dto.PaymentDto;
import com.example.experienceexchange.exception.LessonNotFoundException;
import com.example.experienceexchange.exception.NotAccessException;
import com.example.experienceexchange.exception.SubscriptionNotPossibleException;
import com.example.experienceexchange.model.*;
import com.example.experienceexchange.repository.filter.IFilterProvider;
import com.example.experienceexchange.repository.filter.JpqlFilterProvider;
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

    @Transactional
    @Override
    public List<LessonDto> getLessons(List<SearchCriteria> filters) {
        Map<String, List<SearchCriteria>> searchMap = filterProvider.createSearchMap(filters);
        String filterQuery = filterProvider.createFilterQuery(searchMap);

        List<LessonSingle> lessons = lessonRepository.findAllLessonsByFilter(filterQuery);
        return lessonMapper.toLessonDto(lessons);
    }

    @Transactional
    @Override
    public LessonDto createLesson(JwtUserDetails userDetails, LessonDto lessonDto) {
        Long userId = userDetails.getId();
        User user = userRepository.find(userId);
        LessonSingle newLesson = lessonMapper.lessonDtoToLesson(lessonDto);
        newLesson.setAuthor(user);
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
        updateLesson(lessonBeforeUpdate,lessonAfterUpdate);
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
                Payment payment = paymentMapper.paymentDtoToPayment(paymentDto);
                payment.setDatePayment(DateUtil.dateTimeNow());
                user.addLesson(lesson);
                user.addPayment(payment);
                payment.setLesson(lesson);
                payment.setCostumer(user);
                lesson.increaseNumberSubscriptions();
                paymentRepository.save(payment);
                return paymentMapper.paymentToPaymentDto(payment);
            } else {
                throw new SubscriptionNotPossibleException(SUB_TO_CLOSE_LESSON);
            }
        } else {
            throw new SubscriptionNotPossibleException(SUB_WITH_INCORRECT_PRICE);
        }
    }

    // TODO: НАДО КАК ТО ОТПИСЫВАТЬ ПОЛЬЗОВАТЕЛЕЙ КОГДА ВРЕМЯ КУРСА КОНЧАЕТСЯ
    @Transactional
    @Override
    public LessonDto getLessons(Long lessonId) {
        LessonSingle lesson = getLessonById(lessonId);
        return lessonMapper.lessonToLessonDto(lesson);
    }

    @Transactional
    @Override
    public List<LessonDto> getSchedule(JwtUserDetails userDetails) {
        Long userId = userDetails.getId();
        List<LessonSingle> allLessons = lessonRepository.findAllLessonsByUserId(userId);
        return lessonMapper.toLessonDto(allLessons);
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
}

