package com.example.experienceexchange.service;

import com.example.experienceexchange.dto.LessonDto;
import com.example.experienceexchange.dto.PaymentDto;
import com.example.experienceexchange.exception.IllegalSearchCriteriaException;
import com.example.experienceexchange.exception.LessonNotFoundException;
import com.example.experienceexchange.exception.NotAccessException;
import com.example.experienceexchange.exception.SubscriptionNotPossibleException;
import com.example.experienceexchange.model.Comment;
import com.example.experienceexchange.model.LessonSingle;
import com.example.experienceexchange.model.Payment;
import com.example.experienceexchange.model.User;
import com.example.experienceexchange.repository.filter.IFilterProvider;
import com.example.experienceexchange.repository.filter.SearchCriteria;
import com.example.experienceexchange.repository.interfaceRepo.ILessonRepository;
import com.example.experienceexchange.repository.interfaceRepo.IPaymentRepository;
import com.example.experienceexchange.repository.interfaceRepo.IUserRepository;
import com.example.experienceexchange.security.JwtUserDetails;
import com.example.experienceexchange.util.date.DateUtil;
import com.example.experienceexchange.util.mapper.LessonMapper;
import com.example.experienceexchange.util.mapper.PaymentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.InvalidDataAccessResourceUsageException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LessonServiceTest {

    private static final String FILTER_QUERY = "query";
    private static final Long USER_ID = 1L;
    private static final Long LESSON_ID = 2L;
    private static final Long OTHER_USER_ID = 234L;
    private static final Integer CURRENT_NUMBER_USER = 100;
    private static final BigDecimal CORRECT_PRICE = BigDecimal.ONE;

    @InjectMocks
    private LessonService lessonService;
    @Mock
    private ILessonRepository lessonRepository;
    @Mock
    private IUserRepository userRepository;
    @Mock
    private IPaymentRepository paymentRepository;
    @Mock
    private IFilterProvider filterProvider;
    @Mock
    private LessonMapper lessonMapper;
    @Mock
    private PaymentMapper paymentMapper;
    @Mock
    private DateUtil dateUtil;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_getLessons_when_enteredFilterCorrect() {
        Map<String, List<SearchCriteria>> searchMapMock = mock(Map.class);
        List<SearchCriteria> filters = mock(List.class);
        List<LessonSingle> lessons = mock(List.class);
        List<LessonDto> expectedLessons = mock(List.class);
        when(filterProvider.createSearchMap(filters)).thenReturn(searchMapMock);
        when(filterProvider.createFilterQuery(searchMapMock)).thenReturn(FILTER_QUERY);
        when(lessonRepository.findAllLessonsByFilter(FILTER_QUERY)).thenReturn(lessons);
        when(lessonMapper.toLessonDto(lessons)).thenReturn(expectedLessons);

        List<LessonDto> actual = lessonService.getLessons(filters);

        assertNotNull(actual);
        assertEquals(expectedLessons, actual);
    }

    @Test
    void should_throwIllegalSearchCriteriaException_when_enteredFilterIncorrect() {
        Map<String, List<SearchCriteria>> searchMapMock = mock(Map.class);
        List<SearchCriteria> filters = mock(List.class);
        when(filterProvider.createSearchMap(filters)).thenReturn(searchMapMock);
        when(filterProvider.createFilterQuery(searchMapMock)).thenReturn(FILTER_QUERY);
        when(lessonRepository.findAllLessonsByFilter(FILTER_QUERY)).thenThrow(InvalidDataAccessResourceUsageException.class);

        IllegalSearchCriteriaException exception = assertThrows(IllegalSearchCriteriaException.class, () -> lessonService.getLessons(filters));

        assertEquals(LessonService.ILLEGAL_FILTER, exception.getMessage());
    }

    @Test
    void should_GetLessonInfo_when_enteredLessonIdCorrect() {
        LessonSingle lesson = mock(LessonSingle.class);
        LessonDto expectedLesson = mock(LessonDto.class);
        when(lessonRepository.find(LESSON_ID)).thenReturn(lesson);
        when(lessonMapper.lessonToLessonDto(lesson)).thenReturn(expectedLesson);

        LessonDto actual = lessonService.getLesson(LESSON_ID);

        assertNotNull(actual);
        assertEquals(expectedLesson, actual);
    }

    @Test
    void should_throwLessonNotFoundException_when_enteredLessonIdIncorrect() {
        when(lessonRepository.find(LESSON_ID)).thenReturn(null);

        LessonNotFoundException exceptionInGetLessonMethod = assertThrows(LessonNotFoundException.class, () -> lessonService.getLesson(LESSON_ID));

        assertEquals(String.format("Lesson with id %d not found", LESSON_ID), exceptionInGetLessonMethod.getMessage());
    }

    @Test
    void should_getLessonForSubscriber_when_lessonIsOver() {
        LessonSingle lesson = mock(LessonSingle.class);
        LessonDto expectedLessonDto = mock(LessonDto.class);
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        when(userDetails.getId()).thenReturn(USER_ID);
        when(lessonRepository.findLessonSingleByUserIdAndLessonId(USER_ID, LESSON_ID)).thenReturn(lesson);
        when(lessonMapper.lessonToLessonDto(lesson)).thenReturn(expectedLessonDto);
        when(dateUtil.isDateAfterNow(any())).thenReturn(false);

        LessonDto actual = lessonService.getLesson(userDetails, LESSON_ID);

        assertNotNull(actual);
        assertEquals(expectedLessonDto, actual);
    }

    @Test
    void should_throwNotAccessException_when_lessonNotStarted() {
        LessonSingle lesson = mock(LessonSingle.class);
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        when(userDetails.getId()).thenReturn(USER_ID);
        when(lessonRepository.findLessonSingleByUserIdAndLessonId(USER_ID, LESSON_ID)).thenReturn(lesson);
        when(dateUtil.isDateAfterNow(any())).thenReturn(true);

        NotAccessException exception = assertThrows(NotAccessException.class, () -> lessonService.getLesson(userDetails, LESSON_ID));

        assertEquals(LessonService.LESSON_NOT_START, exception.getMessage());
    }

    @Test
    void should_throwNotAccessException_when_lessonNotFoundInUserSubscriptions() {
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        when(userDetails.getId()).thenReturn(USER_ID);
        when(lessonRepository.findLessonSingleByUserIdAndLessonId(USER_ID, LESSON_ID)).thenReturn(null);

        NotAccessException exception = assertThrows(NotAccessException.class, () -> lessonService.getLesson(userDetails, LESSON_ID));

        assertEquals(LessonService.NOT_FOUND_LESSON_IN_SUBSCRIPTIONS, exception.getMessage());
    }


    @Test
    void should_getScheduleOfAllSubscribedLessons() {
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        List<LessonSingle> lessons = mock(List.class);
        List<LessonDto> expectedLessons = mock(List.class);
        when(userDetails.getId()).thenReturn(USER_ID);
        when(lessonRepository.findAllLessonsByUserId(USER_ID)).thenReturn(lessons);
        when(lessonMapper.toLessonDto(lessons)).thenReturn(expectedLessons);

        List<LessonDto> actual = lessonService.getSchedule(userDetails);

        assertNotNull(actual);
        assertEquals(expectedLessons, actual);
    }

    @Test
    void should_saveNewLessonInDatabase_if_lessonDtoCorrect() {
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        User user = mock(User.class);
        LessonDto enteredLessonDto = mock(LessonDto.class);
        LessonSingle newLesson = mock(LessonSingle.class);
        LessonDto expectedLessonDto = mock(LessonDto.class);
        when(userDetails.getId()).thenReturn(USER_ID);
        when(userRepository.find(USER_ID)).thenReturn(user);
        when(lessonMapper.lessonDtoToLesson(enteredLessonDto)).thenReturn(newLesson);
        when(lessonMapper.lessonToLessonDto(newLesson)).thenReturn(expectedLessonDto);

        LessonDto actual = lessonService.createLesson(userDetails, enteredLessonDto);

        assertNotNull(actual);
        assertEquals(expectedLessonDto, actual);
        verify(lessonRepository).save(newLesson);
        verify(lessonRepository).update(newLesson);
        verify(newLesson).setAuthor(user);
    }

    @Test
    void should_restartLesson_if_lessonIsOverAndUserHasAccess() {
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        User user = mock(User.class);
        LessonDto enteredLessonDto = mock(LessonDto.class);
        LessonSingle oldLesson = mock(LessonSingle.class);
        LessonDto expectedLessonDto = mock(LessonDto.class);
        LessonSingle restartLesson = mock(LessonSingle.class);
        when(userDetails.getId()).thenReturn(USER_ID);
        when(enteredLessonDto.getId()).thenReturn(LESSON_ID);
        when(userRepository.find(USER_ID)).thenReturn(user);
        when(lessonRepository.find(LESSON_ID)).thenReturn(oldLesson);
        when(oldLesson.getAuthor()).thenReturn(user);
        when(user.getId()).thenReturn(USER_ID);
        when(dateUtil.isDateAfterNow(any())).thenReturn(false);
        when(lessonMapper.lessonDtoToLesson(enteredLessonDto)).thenReturn(restartLesson);
        when(lessonRepository.update(restartLesson)).thenReturn(restartLesson);
        when(lessonMapper.lessonToLessonDto(restartLesson)).thenReturn(expectedLessonDto);

        LessonDto actual = lessonService.restartLesson(userDetails, enteredLessonDto);

        verify(lessonRepository).update(restartLesson);
        assertNotNull(actual);
        assertEquals(expectedLessonDto, actual);
    }


    @Test
    void should_throwNotAccessException_when_lessonNotOver() {
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        User user = mock(User.class);
        LessonDto enteredLessonDto = mock(LessonDto.class);
        LessonSingle foundLesson = mock(LessonSingle.class);
        when(userDetails.getId()).thenReturn(USER_ID);
        when(enteredLessonDto.getId()).thenReturn(LESSON_ID);
        when(userRepository.find(USER_ID)).thenReturn(user);
        when(lessonRepository.find(LESSON_ID)).thenReturn(foundLesson);
        when(foundLesson.getAuthor()).thenReturn(user);
        when(user.getId()).thenReturn(USER_ID);
        when(dateUtil.isDateAfterNow(any())).thenReturn(true);

        NotAccessException exceptionInRestartLessonMethod = assertThrows(NotAccessException.class, () -> lessonService.restartLesson(userDetails, enteredLessonDto));

        assertEquals(LessonService.LESSON_NOT_END, exceptionInRestartLessonMethod.getMessage());
    }

    @Test
    void should_throwNotAccessException_when_userDoesNotHaveAccessToEditLesson() {
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        User user = mock(User.class);
        User otherUser = mock(User.class);
        LessonDto enteredLessonDto = mock(LessonDto.class);
        LessonSingle foundLesson = mock(LessonSingle.class);
        when(userDetails.getId()).thenReturn(USER_ID);
        when(enteredLessonDto.getId()).thenReturn(LESSON_ID);
        when(userRepository.find(USER_ID)).thenReturn(user);
        when(lessonRepository.find(LESSON_ID)).thenReturn(foundLesson);
        when(foundLesson.getAuthor()).thenReturn(otherUser);
        when(otherUser.getId()).thenReturn(OTHER_USER_ID);
        when(user.getId()).thenReturn(USER_ID);

        NotAccessException exceptionInRestartMethod = assertThrows(NotAccessException.class, () -> lessonService.restartLesson(userDetails, enteredLessonDto));
        NotAccessException exceptionInEditLessonMethod = assertThrows(NotAccessException.class, () -> lessonService.editLesson(userDetails, LESSON_ID, enteredLessonDto));
        NotAccessException exceptionInDeleteLessonMethod = assertThrows(NotAccessException.class, () -> lessonService.deleteLesson(userDetails, LESSON_ID));

        assertEquals(LessonService.NOT_ACCESS_EDIT, exceptionInRestartMethod.getMessage());
        assertEquals(LessonService.NOT_ACCESS_EDIT, exceptionInEditLessonMethod.getMessage());
        assertEquals(LessonService.NOT_ACCESS_EDIT, exceptionInDeleteLessonMethod.getMessage());
    }


    @Test
    void should_editLesson_when_userHaveAccessToEditLesson() {
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        User user = mock(User.class);
        LessonDto enteredLessonDto = mock(LessonDto.class);
        LessonSingle editLesson = mock(LessonSingle.class);
        LessonSingle foundLesson = mock(LessonSingle.class);
        Set<Comment> comments = mock(Set.class);
        Set<User> usersInLessons = mock(Set.class);
        LessonDto expectedDto = mock(LessonDto.class);
        when(userDetails.getId()).thenReturn(USER_ID);
        when(enteredLessonDto.getId()).thenReturn(LESSON_ID);
        when(userRepository.find(USER_ID)).thenReturn(user);
        when(lessonRepository.find(LESSON_ID)).thenReturn(foundLesson);
        when(foundLesson.getAuthor()).thenReturn(user);
        when(user.getId()).thenReturn(USER_ID);
        when(lessonMapper.lessonDtoToLesson(enteredLessonDto)).thenReturn(editLesson);
        when(foundLesson.getAuthor()).thenReturn(user);
        when(foundLesson.getCurrentNumberUsers()).thenReturn(CURRENT_NUMBER_USER);
        when(foundLesson.getComments()).thenReturn(comments);
        when(foundLesson.getUsersInLesson()).thenReturn(usersInLessons);
        when(lessonRepository.update(editLesson)).thenReturn(editLesson);
        when(lessonMapper.lessonToLessonDto(editLesson)).thenReturn(expectedDto);

        LessonDto actual = lessonService.editLesson(userDetails, LESSON_ID, enteredLessonDto);

        verify(editLesson).setUsersInLesson(usersInLessons);
        verify(editLesson).setComments(comments);
        verify(editLesson).setCurrentNumberUsers(CURRENT_NUMBER_USER);
        assertNotNull(actual);
        assertEquals(expectedDto, actual);
        verify(lessonRepository).update(editLesson);
    }

    @Test
    void should_deleteLesson_when_enteredLessonIdCorrect() {
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        LessonSingle foundLesson = mock(LessonSingle.class);
        User user = mock(User.class);
        when(foundLesson.getAuthor()).thenReturn(user);
        when(lessonRepository.find(LESSON_ID)).thenReturn(foundLesson);
        when(user.getId()).thenReturn(USER_ID);
        when(userDetails.getId()).thenReturn(USER_ID);

        lessonService.deleteLesson(userDetails, LESSON_ID);

        verify(lessonRepository).delete(foundLesson);
    }

    @Test
    void should_subscribeUserToLesson_when_UserIsNotAuthorLessonAndEnteredCorrectPrice() {
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        PaymentDto enteredPaymentDto = mock(PaymentDto.class);
        User user = mock(User.class);
        User authorLesson = mock(User.class);
        Payment newPayment = mock(Payment.class);
        LessonSingle foundLesson = mock(LessonSingle.class);
        PaymentDto expectedPayment = mock(PaymentDto.class);
        when(userDetails.getId()).thenReturn(USER_ID);
        when(lessonRepository.find(LESSON_ID)).thenReturn(foundLesson);
        when(foundLesson.getAuthor()).thenReturn(authorLesson);
        when(authorLesson.getId()).thenReturn(OTHER_USER_ID);
        when(user.getId()).thenReturn(USER_ID);
        when(userRepository.find(USER_ID)).thenReturn(user);
        when(enteredPaymentDto.getPrice()).thenReturn(CORRECT_PRICE);
        when(foundLesson.isSatisfactoryPrice(CORRECT_PRICE)).thenReturn(true);
        when(foundLesson.isAvailableForSubscription(any())).thenReturn(true);
        when(paymentMapper.paymentDtoToPayment(enteredPaymentDto)).thenReturn(newPayment);
        when(paymentMapper.paymentToPaymentDto(newPayment)).thenReturn(expectedPayment);

        PaymentDto actual = lessonService.subscribeToLesson(userDetails, enteredPaymentDto, LESSON_ID);

        assertNotNull(actual);
        assertEquals(expectedPayment, actual);
        verify(newPayment).setLesson(foundLesson);
        verify(newPayment).setCostumer(user);
        verify(foundLesson).increaseNumberSubscriptions();
        verify(paymentRepository).save(newPayment);
    }

    @Test
    void should_throwSubscriptionNotPossibleException_when_UserIsAuthorLesson() {
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        PaymentDto enteredPaymentDto = mock(PaymentDto.class);
        User user = mock(User.class);
        LessonSingle foundLesson = mock(LessonSingle.class);
        when(userDetails.getId()).thenReturn(USER_ID);
        when(lessonRepository.find(LESSON_ID)).thenReturn(foundLesson);
        when(foundLesson.getAuthor()).thenReturn(user);
        when(user.getId()).thenReturn(USER_ID);
        when(userRepository.find(USER_ID)).thenReturn(user);

        SubscriptionNotPossibleException exception = assertThrows(SubscriptionNotPossibleException.class, () -> lessonService.subscribeToLesson(userDetails, enteredPaymentDto, LESSON_ID));

        assertEquals(LessonService.SUB_TO_OWN_LESSON, exception.getMessage());
    }

    @Test
    void should_throwSubscriptionNotPossibleException_when_lessonIsCloseForSub() {
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        PaymentDto enteredPaymentDto = mock(PaymentDto.class);
        User user = mock(User.class);
        User authorLesson = mock(User.class);
        LessonSingle foundLesson = mock(LessonSingle.class);
        when(userDetails.getId()).thenReturn(USER_ID);
        when(lessonRepository.find(LESSON_ID)).thenReturn(foundLesson);
        when(foundLesson.getAuthor()).thenReturn(authorLesson);
        when(authorLesson.getId()).thenReturn(OTHER_USER_ID);
        when(user.getId()).thenReturn(USER_ID);
        when(userRepository.find(USER_ID)).thenReturn(user);
        when(enteredPaymentDto.getPrice()).thenReturn(CORRECT_PRICE);
        when(foundLesson.isSatisfactoryPrice(CORRECT_PRICE)).thenReturn(true);
        when(foundLesson.isAvailableForSubscription(any())).thenReturn(false);

        SubscriptionNotPossibleException exception = assertThrows(SubscriptionNotPossibleException.class, () -> lessonService.subscribeToLesson(userDetails, enteredPaymentDto, LESSON_ID));

        assertEquals(LessonService.SUB_TO_CLOSE_LESSON, exception.getMessage());
    }

    @Test
    void should_throwSubscriptionNotPossibleException_when_priceEnteredByUserIsIncorrect() {
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        PaymentDto enteredPaymentDto = mock(PaymentDto.class);
        User user = mock(User.class);
        User authorLesson = mock(User.class);
        LessonSingle foundLesson = mock(LessonSingle.class);
        when(userDetails.getId()).thenReturn(USER_ID);
        when(lessonRepository.find(LESSON_ID)).thenReturn(foundLesson);
        when(foundLesson.getAuthor()).thenReturn(authorLesson);
        when(authorLesson.getId()).thenReturn(OTHER_USER_ID);
        when(user.getId()).thenReturn(USER_ID);
        when(userRepository.find(USER_ID)).thenReturn(user);
        when(foundLesson.isSatisfactoryPrice(any())).thenReturn(false);

        SubscriptionNotPossibleException exception = assertThrows(SubscriptionNotPossibleException.class, () -> lessonService.subscribeToLesson(userDetails, enteredPaymentDto, LESSON_ID));

        assertEquals(LessonService.SUB_WITH_INCORRECT_PRICE, exception.getMessage());
    }
}