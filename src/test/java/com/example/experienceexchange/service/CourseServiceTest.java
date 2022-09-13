package com.example.experienceexchange.service;

import com.example.experienceexchange.dto.CourseDto;
import com.example.experienceexchange.dto.LessonOnCourseDto;
import com.example.experienceexchange.dto.PaymentDto;
import com.example.experienceexchange.exception.*;
import com.example.experienceexchange.model.Course;
import com.example.experienceexchange.model.LessonOnCourse;
import com.example.experienceexchange.model.Payment;
import com.example.experienceexchange.model.User;
import com.example.experienceexchange.repository.filter.IFilterProvider;
import com.example.experienceexchange.repository.filter.SearchCriteria;
import com.example.experienceexchange.repository.interfaceRepo.ICourseRepository;
import com.example.experienceexchange.repository.interfaceRepo.ILessonOnCourseRepository;
import com.example.experienceexchange.repository.interfaceRepo.IPaymentRepository;
import com.example.experienceexchange.repository.interfaceRepo.IUserRepository;
import com.example.experienceexchange.security.JwtUserDetails;
import com.example.experienceexchange.util.date.DateUtil;
import com.example.experienceexchange.util.mapper.CourseMapper;
import com.example.experienceexchange.util.mapper.LessonOnCourseMapper;
import com.example.experienceexchange.util.mapper.PaymentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.InvalidDataAccessResourceUsageException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CourseServiceTest {

    private static final String FILTER_QUERY = "FILTER";
    private static final Long COURSE_ID = 1L;
    private static final Long USER_ID = 2L;
    private static final Long OTHER_USER_ID = 22L;
    private static final Long LESSON_ID = 3L;
    private static final BigDecimal CORRECT_PRICE = BigDecimal.ONE;

    @InjectMocks
    private CourseService courseService;
    @Mock
    private ICourseRepository courseRepository;
    @Mock
    private IUserRepository userRepository;
    @Mock
    private ILessonOnCourseRepository lessonOnCourseRepository;
    @Mock
    private IPaymentRepository paymentRepository;
    @Mock
    private IFilterProvider filterProvider;
    @Mock
    private PaymentMapper paymentMapper;
    @Mock
    private CourseMapper courseMapper;
    @Mock
    private LessonOnCourseMapper lessonMapper;
    @Mock
    private DateUtil dateUtil;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_getCourses_when_enteredFilterCorrect() {
        Map<String, List<SearchCriteria>> searchMapMock = mock(Map.class);
        List<SearchCriteria> filters = mock(List.class);
        List<Course> courses = mock(List.class);
        List<CourseDto> expectedCourses = mock(List.class);
        when(filterProvider.createSearchMap(filters)).thenReturn(searchMapMock);
        when(filterProvider.createFilterQuery(searchMapMock)).thenReturn(FILTER_QUERY);
        when(courseRepository.findAllCoursesByFilter(FILTER_QUERY)).thenReturn(courses);
        when(courseMapper.toCourseDto(courses)).thenReturn(expectedCourses);

        List<CourseDto> actual = courseService.getCourses(filters);

        assertNotNull(actual);
        assertEquals(expectedCourses, actual);
    }

    @Test
    void should_throwIllegalSearchCriteriaException_when_enteredFilterIncorrect() {
        Map<String, List<SearchCriteria>> searchMapMock = mock(Map.class);
        List<SearchCriteria> filters = mock(List.class);
        when(filterProvider.createSearchMap(filters)).thenReturn(searchMapMock);
        when(filterProvider.createFilterQuery(searchMapMock)).thenReturn(FILTER_QUERY);
        when(courseRepository.findAllCoursesByFilter(FILTER_QUERY)).thenThrow(InvalidDataAccessResourceUsageException.class);

        IllegalSearchCriteriaException exception = assertThrows(IllegalSearchCriteriaException.class, () -> courseService.getCourses(filters));

        assertEquals(CourseService.ILLEGAL_FILTER, exception.getMessage());
    }

    @Test
    void should_GetCourseInfo_when_enteredCourseIdCorrect() {
        Course course = mock(Course.class);
        CourseDto expectedCourse = mock(CourseDto.class);
        when(courseRepository.find(COURSE_ID)).thenReturn(course);
        when(courseMapper.courseToCourseDto(course)).thenReturn(expectedCourse);

        CourseDto actual = courseService.getCourse(COURSE_ID);

        assertNotNull(actual);
        assertEquals(expectedCourse, actual);
    }

    @Test
    void should_throwCourseNotFoundException_when_enteredCourseIdIncorrect() {
        when(courseRepository.find(COURSE_ID)).thenReturn(null);

        CourseNotFoundException exception = assertThrows(CourseNotFoundException.class, () -> courseService.getCourse(COURSE_ID));

        assertEquals(String.format("Course with id %d not found", COURSE_ID), exception.getMessage());
    }

    @Test
    void should_getScheduleOfAllSubscribedCourses() {
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        List<LessonOnCourse> lessonsOnSubscribedCourses = mock(List.class);
        List<LessonOnCourseDto> expectedLessons = mock(List.class);
        when(userDetails.getId()).thenReturn(USER_ID);
        when(lessonOnCourseRepository.findAllLessonsOnSubscribedCoursesByUserId(USER_ID)).thenReturn(lessonsOnSubscribedCourses);
        when(lessonMapper.toLessonOnCourseDto(lessonsOnSubscribedCourses)).thenReturn(expectedLessons);

        List<LessonOnCourseDto> actual = courseService.getSchedule(userDetails);

        assertNotNull(actual);
        assertEquals(expectedLessons, actual);
    }

    @Test
    void should_getScheduleOfCourse() {
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        List<LessonOnCourse> lessonsOnSubscribedCourse = mock(List.class);
        List<LessonOnCourseDto> expectedLessons = mock(List.class);
        when(userDetails.getId()).thenReturn(USER_ID);
        when(lessonOnCourseRepository.findAllLessonsOnCourseByUserIdAndCourseId(USER_ID, COURSE_ID)).thenReturn(lessonsOnSubscribedCourse);
        when(lessonMapper.toLessonOnCourseDto(lessonsOnSubscribedCourse)).thenReturn(expectedLessons);

        List<LessonOnCourseDto> actual = courseService.getScheduleByCourse(userDetails, COURSE_ID);

        assertNotNull(actual);
        assertEquals(expectedLessons, actual);
    }

    @Test
    void should_geLessonOnCourse_when_userHasAccessToLesson() {
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        LessonOnCourse foundLesson = mock(LessonOnCourse.class);
        LessonOnCourseDto expectedLesson = mock(LessonOnCourseDto.class);
        Date date = Date.from(Instant.now());
        when(userDetails.getId()).thenReturn(USER_ID);
        when(lessonOnCourseRepository.findLessonInCourseForSubscriber(USER_ID, COURSE_ID, LESSON_ID)).thenReturn(foundLesson);
        when(dateUtil.isDateAfterNow(any())).thenReturn(false);
        when(dateUtil.isDateBeforeNow(any())).thenReturn(false);
        when(dateUtil.addDays(any(), any())).thenReturn(date);
        when(lessonMapper.lessonOnCourseToLessonOnCourseDto(foundLesson)).thenReturn(expectedLesson);

        LessonOnCourseDto actual = courseService.getLessonOnCourse(userDetails, COURSE_ID, LESSON_ID);

        assertNotNull(actual);
        assertEquals(expectedLesson, actual);
    }

    @Test
    void should_throwNotAccessException_when_userDoesNotHaveAccessToLesson() {
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        LessonOnCourse foundLesson = mock(LessonOnCourse.class);
        Date date = Date.from(Instant.now());
        when(userDetails.getId()).thenReturn(USER_ID);
        when(lessonOnCourseRepository.findLessonInCourseForSubscriber(USER_ID, COURSE_ID, LESSON_ID)).thenReturn(foundLesson);
        when(dateUtil.isDateAfterNow(any())).thenReturn(true);
        when(dateUtil.isDateBeforeNow(any())).thenReturn(false);
        when(dateUtil.addDays(any(), any())).thenReturn(date);

        NotAccessException exception = assertThrows(NotAccessException.class, () -> courseService.getLessonOnCourse(userDetails, COURSE_ID, LESSON_ID));

        assertEquals(CourseService.NOT_ACCESS_TO_LESSON, exception.getMessage());
    }

    @Test
    void should_throwLessonNotFoundException_when_enteredLessonIdIncorrect() {
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        when(userDetails.getId()).thenReturn(USER_ID);
        when(lessonOnCourseRepository.findLessonInCourseForSubscriber(USER_ID, COURSE_ID, LESSON_ID)).thenReturn(null);

        LessonNotFoundException exception = assertThrows(LessonNotFoundException.class, () -> courseService.getLessonOnCourse(userDetails, COURSE_ID, LESSON_ID));

        assertEquals(String.format("Lesson with id %d not found", LESSON_ID), exception.getMessage());
    }

    @Test
    void should_restartCourses_if_userHasAccessToCourseEdit() {
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        CourseDto enteredCourseDto = mock(CourseDto.class);
        Course courseToRestart = mock(Course.class);
        Course updatedCourse = mock(Course.class);
        User user = mock(User.class);
        CourseDto expectedCourseDto = mock(CourseDto.class);
        when(enteredCourseDto.getId()).thenReturn(COURSE_ID);
        when(userDetails.getId()).thenReturn(USER_ID);
        when(userRepository.find(USER_ID)).thenReturn(user);
        when(courseRepository.find(COURSE_ID)).thenReturn(courseToRestart);
        when(courseToRestart.getAuthor()).thenReturn(user);
        when(user.getId()).thenReturn(USER_ID);
        when(dateUtil.isDateAfterNow(any())).thenReturn(false);
        when(courseMapper.courseDtoToCourse(enteredCourseDto)).thenReturn(updatedCourse);
        when(courseRepository.update(updatedCourse)).thenReturn(updatedCourse);
        when(courseMapper.courseToCourseDto(updatedCourse)).thenReturn(expectedCourseDto);

        CourseDto actual = courseService.restartCourse(userDetails, enteredCourseDto);

        verify(courseRepository).update(updatedCourse);
        assertNotNull(actual);
        assertEquals(expectedCourseDto, actual);
    }

    @Test
    void should_throwNotAccessException_when_userDoesNotHaveAccessToCourseEdit() {
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        CourseDto enteredCourseDto = mock(CourseDto.class);
        Course courseToRestart = mock(Course.class);
        User user = mock(User.class);
        User other_user = mock(User.class);
        when(enteredCourseDto.getId()).thenReturn(COURSE_ID);
        when(userDetails.getId()).thenReturn(USER_ID);
        when(userRepository.find(USER_ID)).thenReturn(user);
        when(courseRepository.find(COURSE_ID)).thenReturn(courseToRestart);
        when(courseToRestart.getAuthor()).thenReturn(other_user);
        when(other_user.getId()).thenReturn(OTHER_USER_ID);
        when(user.getId()).thenReturn(USER_ID);

        NotAccessException exceptionInRestartCourseMethod = assertThrows(NotAccessException.class, () -> courseService.restartCourse(userDetails, enteredCourseDto));
        NotAccessException exceptionInEditCourseMethod = assertThrows(NotAccessException.class, () -> courseService.editCourse(userDetails, COURSE_ID, enteredCourseDto));

        assertEquals(CourseService.NOT_ACCESS_EDIT, exceptionInEditCourseMethod.getMessage());
        assertEquals(CourseService.NOT_ACCESS_EDIT, exceptionInRestartCourseMethod.getMessage());
    }

    @Test
    void should_throwNotAccessException_if_courseNotFinished() {
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        CourseDto enteredCourseDto = mock(CourseDto.class);
        Course courseToRestart = mock(Course.class);
        User user = mock(User.class);
        when(enteredCourseDto.getId()).thenReturn(COURSE_ID);
        when(userDetails.getId()).thenReturn(USER_ID);
        when(userRepository.find(USER_ID)).thenReturn(user);
        when(courseRepository.find(COURSE_ID)).thenReturn(courseToRestart);
        when(courseToRestart.getAuthor()).thenReturn(user);
        when(user.getId()).thenReturn(USER_ID);
        when(dateUtil.isDateAfterNow(any())).thenReturn(true);

        NotAccessException exception = assertThrows(NotAccessException.class, () -> courseService.restartCourse(userDetails, enteredCourseDto));

        assertEquals(CourseService.COURSE_NOT_END, exception.getMessage());
    }

    @Test
    void should_saveNewCourseInDatabase_when_enteredCourseDtoCorrect() {
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        CourseDto enteredCourseDto = mock(CourseDto.class);
        Course newCourse = mock(Course.class);
        User user = mock(User.class);
        CourseDto expectedCourseDto = mock(CourseDto.class);
        when(userDetails.getId()).thenReturn(USER_ID);
        when(userRepository.find(USER_ID)).thenReturn(user);
        when(courseMapper.courseDtoToCourse(enteredCourseDto)).thenReturn(newCourse);
        when(courseMapper.courseToCourseDto(newCourse)).thenReturn(expectedCourseDto);

        CourseDto actual = courseService.createCourse(userDetails, enteredCourseDto);

        assertNotNull(actual);
        assertEquals(expectedCourseDto, actual);
        verify(courseRepository).save(newCourse);
        verify(courseRepository).update(newCourse);
    }

    @Test
    void should_editCourse_when_enteredCourseDtoCorrect() {
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        CourseDto enteredCourseDto = mock(CourseDto.class);
        Course courseAfterUpdate = mock(Course.class);
        Course courseBeforeUpdate = mock(Course.class);
        CourseDto expectedCourseDto = mock(CourseDto.class);
        User user = mock(User.class);
        when(courseRepository.find(COURSE_ID)).thenReturn(courseBeforeUpdate);
        when(courseBeforeUpdate.getAuthor()).thenReturn(user);
        when(user.getId()).thenReturn(USER_ID);
        when(userDetails.getId()).thenReturn(USER_ID);
        when(courseMapper.courseDtoToCourse(enteredCourseDto)).thenReturn(courseAfterUpdate);
        when(courseRepository.update(courseAfterUpdate)).thenReturn(courseAfterUpdate);
        when(courseMapper.courseToCourseDto(courseAfterUpdate)).thenReturn(expectedCourseDto);

        CourseDto actual = courseService.editCourse(userDetails, COURSE_ID, enteredCourseDto);

        assertNotNull(actual);
        assertEquals(expectedCourseDto, actual);
        verify(courseAfterUpdate).setAuthor(any());
        verify(courseAfterUpdate).setCurrentNumberUsers(any());
        verify(courseAfterUpdate).setComments(any());
        verify(courseAfterUpdate).setUsersInCourse(any());
        verify(courseRepository).update(courseAfterUpdate);
    }

    @Test
    void should_deletedCourse_when_enteredCourseIdCorrect() {
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        Course foundCourse = mock(Course.class);
        User user = mock(User.class);
        when(courseRepository.find(COURSE_ID)).thenReturn(foundCourse);
        when(foundCourse.getAuthor()).thenReturn(user);
        when(user.getId()).thenReturn(USER_ID);
        when(userDetails.getId()).thenReturn(USER_ID);

        courseService.deleteCourse(userDetails, COURSE_ID);

        verify(courseRepository).delete(foundCourse);
    }

    @Test
    void should_subscribeToCourseUser_when_userIsNotAuthorCourseAndEnteredCorrectPrice() {
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        PaymentDto enteredPaymentDto = mock(PaymentDto.class);
        Payment newPayment = mock(Payment.class);
        PaymentDto expectedPayment = mock(PaymentDto.class);
        Course course = mock(Course.class);
        User author = mock(User.class);
        User user = mock(User.class);
        when(enteredPaymentDto.getPrice()).thenReturn(CORRECT_PRICE);
        when(courseRepository.find(COURSE_ID)).thenReturn(course);
        when(userRepository.find(USER_ID)).thenReturn(user);
        when(course.getAuthor()).thenReturn(author);
        when(author.getId()).thenReturn(OTHER_USER_ID);
        when(userDetails.getId()).thenReturn(USER_ID);
        when(course.isSatisfactoryPrice(CORRECT_PRICE)).thenReturn(true);
        when(course.isAvailableForSubscription(any())).thenReturn(true);
        when(paymentMapper.paymentDtoToPayment(enteredPaymentDto)).thenReturn(newPayment);
        when(paymentMapper.paymentToPaymentDto(newPayment)).thenReturn(expectedPayment);


        PaymentDto actual = courseService.subscribeToCourse(userDetails, enteredPaymentDto, COURSE_ID);

        assertNotNull(actual);
        assertEquals(expectedPayment, actual);
        verify(newPayment).setCourse(course);
        verify(newPayment).setCostumer(user);
        verify(course).increaseNumberSubscriptions();
        verify(paymentRepository).save(newPayment);
    }

    @Test
    void should_throwSubscriptionNotPossibleException_when_UserIsAuthorCourse() {
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        PaymentDto enteredPaymentDto = mock(PaymentDto.class);
        User user = mock(User.class);
        Course foundCourse = mock(Course.class);
        when(userDetails.getId()).thenReturn(USER_ID);
        when(courseRepository.find(COURSE_ID)).thenReturn(foundCourse);
        when(foundCourse.getAuthor()).thenReturn(user);
        when(user.getId()).thenReturn(USER_ID);
        when(userRepository.find(USER_ID)).thenReturn(user);

        SubscriptionNotPossibleException exception = assertThrows(SubscriptionNotPossibleException.class, () -> courseService.subscribeToCourse(userDetails, enteredPaymentDto, COURSE_ID));

        assertEquals(CourseService.SUB_TO_OWN_COURSE, exception.getMessage());
    }

    @Test
    void should_throwSubscriptionNotPossibleException_when_CourseIsCloseForSub() {
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        PaymentDto enteredPaymentDto = mock(PaymentDto.class);
        User user = mock(User.class);
        User authorCourse = mock(User.class);
        Course foundCourse = mock(Course.class);
        when(userDetails.getId()).thenReturn(USER_ID);
        when(courseRepository.find(COURSE_ID)).thenReturn(foundCourse);
        when(foundCourse.getAuthor()).thenReturn(authorCourse);
        when(authorCourse.getId()).thenReturn(OTHER_USER_ID);
        when(user.getId()).thenReturn(USER_ID);
        when(userRepository.find(USER_ID)).thenReturn(user);
        when(enteredPaymentDto.getPrice()).thenReturn(CORRECT_PRICE);
        when(foundCourse.isSatisfactoryPrice(CORRECT_PRICE)).thenReturn(true);
        when(foundCourse.isAvailableForSubscription(any())).thenReturn(false);

        SubscriptionNotPossibleException exception = assertThrows(SubscriptionNotPossibleException.class, () -> courseService.subscribeToCourse(userDetails, enteredPaymentDto, COURSE_ID));

        assertEquals(CourseService.SUB_TO_CLOSE_COURSE, exception.getMessage());
    }

    @Test
    void should_throwSubscriptionNotPossibleException_when_priceEnteredByUserIsIncorrect() {
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        PaymentDto enteredPaymentDto = mock(PaymentDto.class);
        User user = mock(User.class);
        User authorCourse = mock(User.class);
        Course foundCourse = mock(Course.class);
        when(userDetails.getId()).thenReturn(USER_ID);
        when(courseRepository.find(COURSE_ID)).thenReturn(foundCourse);
        when(foundCourse.getAuthor()).thenReturn(authorCourse);
        when(authorCourse.getId()).thenReturn(OTHER_USER_ID);
        when(user.getId()).thenReturn(USER_ID);
        when(userRepository.find(USER_ID)).thenReturn(user);
        when(foundCourse.isSatisfactoryPrice(any())).thenReturn(false);

        SubscriptionNotPossibleException exception = assertThrows(SubscriptionNotPossibleException.class, () -> courseService.subscribeToCourse(userDetails, enteredPaymentDto, COURSE_ID));

        assertEquals(CourseService.SUB_WITH_INCORRECT_PRICE, exception.getMessage());
    }

    @Test
    void should_createLessonOnCourse_when_userHasAccessToCourseAndCourseIdCorrect() {
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        LessonOnCourseDto enteredLessonOnCourseDto = mock(LessonOnCourseDto.class);
        LessonOnCourse newLesson = mock(LessonOnCourse.class);
        User authorCourse = mock(User.class);
        Course foundCourse = mock(Course.class);
        CourseDto expectedCourse = mock(CourseDto.class);
        when(userDetails.getId()).thenReturn(USER_ID);
        when(courseRepository.find(COURSE_ID)).thenReturn(foundCourse);
        when(foundCourse.getAuthor()).thenReturn(authorCourse);
        when(authorCourse.getId()).thenReturn(USER_ID);
        when(lessonMapper.lessonOnCourseDtoToLessonOnCourse(enteredLessonOnCourseDto)).thenReturn(newLesson);
        when(courseMapper.courseToCourseDto(foundCourse)).thenReturn(expectedCourse);

        CourseDto actual = courseService.createLessonOnCourse(userDetails, COURSE_ID, enteredLessonOnCourseDto);

        assertNotNull(actual);
        assertEquals(expectedCourse, actual);
        verify(newLesson).setCourse(foundCourse);
        verify(foundCourse).addLesson(newLesson);
        verify(lessonOnCourseRepository).save(newLesson);
    }
}