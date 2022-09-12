package com.example.experienceexchange.service;

import com.example.experienceexchange.dto.*;
import com.example.experienceexchange.exception.EmailNotUniqueException;
import com.example.experienceexchange.exception.PasswordsNotMatchException;
import com.example.experienceexchange.model.Course;
import com.example.experienceexchange.model.LessonSingle;
import com.example.experienceexchange.model.Payment;
import com.example.experienceexchange.model.User;
import com.example.experienceexchange.repository.interfaceRepo.IUserRepository;
import com.example.experienceexchange.security.JwtUserDetails;
import com.example.experienceexchange.util.mapper.CourseMapper;
import com.example.experienceexchange.util.mapper.LessonMapper;
import com.example.experienceexchange.util.mapper.PaymentMapper;
import com.example.experienceexchange.util.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private static final Long USER_ID = 1L;
    private static final String PASSWORD_EQUAL = "password";
    private static final String PASSWORD_NOT_EQUAL = "NOT_password";
    private static final String ENCODE_PASSWORD = "encode";
    private static final String NEW_EMAIL = "EMAIL";

    @InjectMocks
    private UserService userService;
    @Mock
    private IUserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PaymentMapper paymentMapper;
    @Mock
    private LessonMapper lessonMapper;
    @Mock
    private CourseMapper courseMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_getAccountByUserId() {
        User user = mock(User.class);
        UserDto expectedDto = mock(UserDto.class);
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        when(userDetails.getId()).thenReturn(USER_ID);
        when(userRepository.find(USER_ID)).thenReturn(user);
        when(userMapper.userToUserDto(user)).thenReturn(expectedDto);

        UserDto actual = userService.getAccount(userDetails);

        assertNotNull(actual);
        assertEquals(expectedDto, actual);
    }

    @Test
    void should_getCreatedCoursesByUser() {
        User author = mock(User.class);
        Set<Course> createdCourses = mock(Set.class);
        List<CourseDto> expectedList = mock(List.class);
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        when(userDetails.getId()).thenReturn(USER_ID);
        when(userRepository.find(USER_ID)).thenReturn(author);
        when(author.getCreatedCourses()).thenReturn(createdCourses);
        when(courseMapper.toCourseDto(createdCourses)).thenReturn(expectedList);

        List<CourseDto> actual = userService.getCreatedCoursesByUser(userDetails);

        assertNotNull(actual);
        assertEquals(expectedList, actual);
    }

    @Test
    void should_getPaymentsByUser() {
        User author = mock(User.class);
        List<Payment> payments = mock(List.class);
        List<PaymentDto> expectedList = mock(List.class);
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        when(userDetails.getId()).thenReturn(USER_ID);
        when(userRepository.find(USER_ID)).thenReturn(author);
        when(author.getMyPayments()).thenReturn(payments);
        when(paymentMapper.toPaymentDto(payments)).thenReturn(expectedList);

        List<PaymentDto> actual = userService.getPayments(userDetails);

        assertNotNull(actual);
        assertEquals(expectedList, actual);
    }

    @Test
    void should_getCreatedLessonsByUser() {
        User author = mock(User.class);
        Set<LessonSingle> createdLessons = mock(Set.class);
        List<LessonDto> expectedList = mock(List.class);
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        when(userDetails.getId()).thenReturn(USER_ID);
        when(userRepository.find(USER_ID)).thenReturn(author);
        when(author.getCreatedLessons()).thenReturn(createdLessons);
        when(lessonMapper.toLessonDto(createdLessons)).thenReturn(expectedList);

        List<LessonDto> actual = userService.getCreatedLessonsByUser(userDetails);

        assertNotNull(actual);
        assertEquals(expectedList, actual);
    }

    @Test
    void should_getLessonsSubscribedByUser() {
        User author = mock(User.class);
        Set<LessonSingle> subscriptions = mock(Set.class);
        List<LessonDto> expectedList = mock(List.class);
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        when(userDetails.getId()).thenReturn(USER_ID);
        when(userRepository.find(USER_ID)).thenReturn(author);
        when(author.getLessonSubscriptions()).thenReturn(subscriptions);
        when(lessonMapper.toLessonDto(subscriptions)).thenReturn(expectedList);

        List<LessonDto> actual = userService.getLessonsSubscribedByUser(userDetails);

        assertNotNull(actual);
        assertEquals(expectedList, actual);
    }

    @Test
    void should_getCoursesSubscribedByUser() {
        User author = mock(User.class);
        Set<Course> subscriptions = mock(Set.class);
        List<CourseDto> expectedList = mock(List.class);
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        when(userDetails.getId()).thenReturn(USER_ID);
        when(userRepository.find(USER_ID)).thenReturn(author);
        when(author.getCourseSubscriptions()).thenReturn(subscriptions);
        when(courseMapper.toCourseDto(subscriptions)).thenReturn(expectedList);

        List<CourseDto> actual = userService.getCoursesSubscribedByUser(userDetails);

        assertNotNull(actual);
        assertEquals(expectedList, actual);
    }

    @Test
    void should_editAccount() {
        User user = mock(User.class);
        UserDto enteredUserDto = mock(UserDto.class);
        UserDto expectedDto = mock(UserDto.class);
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        when(userDetails.getId()).thenReturn(USER_ID);
        when(userRepository.find(USER_ID)).thenReturn(user);
        when(userMapper.updateUser(user, enteredUserDto)).thenReturn(user);
        when(userMapper.userToUserDto(user)).thenReturn(expectedDto);

        UserDto actual = userService.editAccount(userDetails, enteredUserDto);

        verify(userRepository).update(user);
        assertNotNull(actual);
        assertEquals(expectedDto, actual);
    }

    @Test
    void should_changePasswordOfUser_when_enteredPasswordsMatch() {
        User user = mock(User.class);
        NewPasswordDto passwordDto = mock(NewPasswordDto.class);
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        when(userDetails.getId()).thenReturn(USER_ID);
        when(passwordDto.getNewPassword()).thenReturn(PASSWORD_EQUAL);
        when(passwordDto.getNewPasswordSecond()).thenReturn(PASSWORD_EQUAL);
        when(passwordEncoder.encode(passwordDto.getNewPassword())).thenReturn(ENCODE_PASSWORD);
        when(userRepository.find(USER_ID)).thenReturn(user);

        userService.changePassword(userDetails, passwordDto);

        verify(user).setPassword(ENCODE_PASSWORD);
        verify(userRepository).update(user);
    }

    @Test
    void should_throwPasswordNotMatchException_when_enteredPasswordsNotMatch() {
        NewPasswordDto passwordDto = mock(NewPasswordDto.class);
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        when(passwordDto.getNewPassword()).thenReturn(PASSWORD_EQUAL);
        when(passwordDto.getNewPasswordSecond()).thenReturn(PASSWORD_NOT_EQUAL);

        PasswordsNotMatchException exception = assertThrows(PasswordsNotMatchException.class, () -> userService.changePassword(userDetails, passwordDto));

        assertEquals("Passwords do not match", exception.getMessage());
    }

    @Test
    void should_changeEmailOfUser_when_enteredEmailUnique() {
        User user = mock(User.class);
        NewEmailDto emailDto = mock(NewEmailDto.class);
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        when(userDetails.getId()).thenReturn(USER_ID);
        when(emailDto.getNewEmail()).thenReturn(NEW_EMAIL);
        when(userRepository.findByEmail(NEW_EMAIL)).thenReturn(null);
        when(userRepository.find(USER_ID)).thenReturn(user);

        userService.changeEmail(userDetails, emailDto);

        verify(user).setEmail(NEW_EMAIL);
        verify(userRepository).update(user);
    }

    @Test
    void should_throwEmailNotUniqueException_when_enteredEmailNotUnique() {
        User user = mock(User.class);
        NewEmailDto emailDto = mock(NewEmailDto.class);
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        when(emailDto.getNewEmail()).thenReturn(NEW_EMAIL);
        when(userRepository.findByEmail(NEW_EMAIL)).thenReturn(user);

        EmailNotUniqueException exception = assertThrows(EmailNotUniqueException.class, () -> userService.changeEmail(userDetails, emailDto));

        assertEquals("Email is not unique", exception.getMessage());
    }
}