package com.example.experienceexchange.service;

import com.example.experienceexchange.dto.CommentDto;
import com.example.experienceexchange.dto.LessonDto;
import com.example.experienceexchange.dto.PaymentDto;
import com.example.experienceexchange.exception.LessonNotFoundException;
import com.example.experienceexchange.exception.NotAccessException;
import com.example.experienceexchange.exception.SubscriptionNotPossibleException;
import com.example.experienceexchange.model.Comment;
import com.example.experienceexchange.model.LessonSingle;
import com.example.experienceexchange.model.Payment;
import com.example.experienceexchange.model.User;
import com.example.experienceexchange.repository.PaymentRepository;
import com.example.experienceexchange.repository.interfaceRepo.ICommentRepository;
import com.example.experienceexchange.repository.interfaceRepo.ILessonRepository;
import com.example.experienceexchange.repository.interfaceRepo.IPaymentRepository;
import com.example.experienceexchange.repository.interfaceRepo.IUserRepository;
import com.example.experienceexchange.security.JwtUserDetails;
import com.example.experienceexchange.service.interfaceService.ILessonService;
import com.example.experienceexchange.util.date.DateUtil;
import com.example.experienceexchange.util.mapper.CommentMapper;
import com.example.experienceexchange.util.mapper.LessonMapper;
import com.example.experienceexchange.util.mapper.PaymentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class LessonService implements ILessonService {

    private static final String SUB_TO_OWN_LESSON = "Subscription to own lesson is not possible";
    private static final String SUB_TO_CLOSE_LESSON = "Lesson is closed for subscription";
    private static final String SUB_WITH_INCORRECT_PRICE = "Entered price is less than the fixed price";
    private static final String NOT_DELETE_STARTED_COURSE ="Course started can`t be deleted";

    private final ILessonRepository lessonRepository;
    private final IUserRepository userRepository;
    private final LessonMapper lessonMapper;
    private final PaymentMapper paymentMapper;
    private final IPaymentRepository paymentRepository; // сделать отдельный контроллер под это

    public LessonService(ILessonRepository lessonRepository,
                         IUserRepository userRepository,
                         LessonMapper lessonMapper,
                         PaymentMapper paymentMapper,
                         IPaymentRepository paymentRepository) {
        this.lessonRepository = lessonRepository;
        this.userRepository = userRepository;
        this.lessonMapper = lessonMapper;
        this.paymentMapper = paymentMapper;
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    @Override
    public LessonDto createLesson(JwtUserDetails userDetails, LessonDto lessonDto) {
        Long userId = userDetails.getId();
        User user = userRepository.find(userId);
        LessonSingle newLesson = lessonMapper.lessonDtoToLesson(lessonDto);
        newLesson.setAuthor(user);
        lessonRepository.save(newLesson);
        LessonSingle update = lessonRepository.update(newLesson); // надо ли так сохранять (ссылка на update) ?
        return lessonMapper.lessonToLessonDto(update);

    }

    // TODO : ПРОВЕРИТЬ ДАТЫ
    @Transactional
    @Override
    public LessonDto editLesson(JwtUserDetails userDetails, Long id, LessonDto lessonDto) {
        LessonSingle oldLesson = getLessonById(id);
        checkAccessToLessonEdit(oldLesson.getAuthor().getId(), userDetails.getId());
        LessonSingle updateLesson = lessonMapper.lessonDtoToLesson(lessonDto);
        updateLesson.setAuthor(oldLesson.getAuthor());
        updateLesson.setCurrentNumberUsers(oldLesson.getCurrentNumberUsers());
        updateLesson.setComments(oldLesson.getComments());
        updateLesson.setUsersInLesson(oldLesson.getUsersInLesson());
        LessonSingle update = lessonRepository.update(updateLesson);
        return lessonMapper.lessonToLessonDto(update);
    }
    // ЕСЛИ КУРС УДАЛИТСЯ ТО ЧТО ДЕЛАТЬ С ДЕНЬГАМИ ПОКУПАТЕЛЕЙ?
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
    public List<LessonDto> getLessonByDirection() {
        List<LessonSingle> lessons = lessonRepository.findAll();
        return lessonMapper.toLessonDto(lessons);
    }

    // TODO : НЕЛЬЗЯ СДЕЛАТЬ НЕСКОЛЬКО ОПЛАТ ЗА ОДИН КУРС В ОДИН ПЕРИОД) МОЖЕТ БЫТЬ СДЕЛАТЬ ДОНАТЫ И ТОГДА ПЛАТЕЖИ ПРОВЕРЯТЬ НЕ НАДО БУДЕТЁ
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
    // TODO : ПАРАМЕТР ДОСТУПА У LESSON ON COURSE НЕ ИПСОЛЬЗУЕТСЯ ( КОЛИЧЕСТВО ДНЕЙ НА УРОК)
    @Transactional
    @Override
    public LessonDto getLesson(Long lessonId) {
        LessonSingle lesson = getLessonById(lessonId);
        return lessonMapper.lessonToLessonDto(lesson);
    }

    private LessonSingle getLessonById(Long lessonId) throws LessonNotFoundException {
        LessonSingle lesson = lessonRepository.find(lessonId);
        if (lesson == null) {
            throw new LessonNotFoundException(lessonId);
        }
        return lesson;
    }

    private void checkAccessToLessonEdit(Long authorId, Long authUserID) {
        if (!authorId.equals(authUserID)) {
            throw new NotAccessException();
        }
    }
}

