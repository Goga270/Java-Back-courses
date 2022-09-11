package com.example.experienceexchange.service;

import com.example.experienceexchange.dto.*;
import com.example.experienceexchange.exception.EmailNotUniqueException;
import com.example.experienceexchange.exception.PasswordsNotMatchException;
import com.example.experienceexchange.exception.UserNotFoundException;
import com.example.experienceexchange.model.Course;
import com.example.experienceexchange.model.LessonSingle;
import com.example.experienceexchange.model.Payment;
import com.example.experienceexchange.model.User;
import com.example.experienceexchange.repository.interfaceRepo.IUserRepository;
import com.example.experienceexchange.security.JwtUserDetails;
import com.example.experienceexchange.service.interfaceService.IUserService;
import com.example.experienceexchange.util.mapper.CourseMapper;
import com.example.experienceexchange.util.mapper.LessonMapper;
import com.example.experienceexchange.util.mapper.PaymentMapper;
import com.example.experienceexchange.util.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserService implements IUserService {

    private final IUserRepository userRepository;
    private final UserMapper userMapper;
    private final PaymentMapper paymentMapper;
    private final LessonMapper lessonMapper;
    private final CourseMapper courseMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(IUserRepository userRepository,
                       UserMapper userMapper,
                       PaymentMapper paymentMapper,
                       LessonMapper lessonMapper,
                       CourseMapper courseMapper,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.paymentMapper = paymentMapper;
        this.lessonMapper = lessonMapper;
        this.courseMapper = courseMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto getAccount(JwtUserDetails userDetails) {
        log.debug("Get account for user {}", userDetails.getId());
        Long userId = userDetails.getId();
        User user = getUserById(userId);

        return userMapper.userToUserDto(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CourseDto> getCreatedCoursesByUser(JwtUserDetails userDetails) {
        log.debug("Get courses created by user {} ", userDetails.getId());
        User author = getUserById(userDetails.getId());
        Set<Course> courses = author.getCreatedCourses();

        return courseMapper.toCourseDto(courses);
    }

    @Transactional(readOnly = true)
    @Override
    public List<PaymentDto> getPayments(JwtUserDetails userDetails) {
        log.debug("Get payments for user {}", userDetails.getId());
        List<Payment> payments = getUserById(userDetails.getId()).getMyPayments();
        return paymentMapper.toPaymentDto(payments);
    }

    @Transactional(readOnly = true)
    @Override
    public List<LessonDto> getLessonsSubscribedByUser(JwtUserDetails userDetails) {
        log.debug("Get lessons user {} is subscribed to", userDetails.getId());
        User user = getUserById(userDetails.getId());
        Set<LessonSingle> lessonSubscriptions = user.getLessonSubscriptions();
        return lessonMapper.toLessonDto(lessonSubscriptions);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CourseDto> getCoursesSubscribedByUser(JwtUserDetails userDetails) {
        log.debug("Get courses user {} is subscribed to", userDetails.getId());
        User user = getUserById(userDetails.getId());
        Set<Course> courseSubscriptions = user.getCourseSubscriptions();
        return courseMapper.toCourseDto(courseSubscriptions);
    }

    @Transactional(readOnly = true)
    @Override
    public List<LessonDto> getCreatedLessonsByUser(JwtUserDetails userDetails) {
        log.debug("Get lessons created by user {} ", userDetails.getId());
        User author = getUserById(userDetails.getId());
        Set<LessonSingle> lessons = author.getCreatedLessons();

        return lessonMapper.toLessonDto(lessons);
    }

    @Transactional
    @Override
    public UserDto editAccount(JwtUserDetails userDetails, UserDto userDto) {
        log.debug("Edit user account {}", userDetails.getId());
        Long userId = userDetails.getId();
        User oldUser = getUserById(userId);
        User newUser = userMapper.updateUser(oldUser, userDto);

        userRepository.update(newUser);
        return userMapper.userToUserDto(newUser);
    }

    @Transactional
    @Override
    public void changePassword(JwtUserDetails userDetails, NewPasswordDto passwordDto) {
        log.debug("Change user {} password", userDetails.getId());
        if (passwordDto.getNewPassword().equals(passwordDto.getNewPasswordSecond())) {
            User user = userRepository.find(userDetails.getId());
            String encodeNewPassword = passwordEncoder.encode(passwordDto.getNewPassword());
            user.setPassword(encodeNewPassword);
            userRepository.update(user);
        } else {
            log.warn("Password not match, password not changed");
            throw new PasswordsNotMatchException();
        }
    }

    @Transactional
    @Override
    public void changeEmail(JwtUserDetails jwtUserDetails, NewEmailDto newEmailDto) {
        log.debug("Change user {} email", jwtUserDetails.getId());
        User user = userRepository.findByEmail(newEmailDto.getNewEmail());
        if (user != null) {
            log.warn("New email for user {} is not unique, email not changed", jwtUserDetails.getId());
            throw new EmailNotUniqueException();
        }
        User userForUpdate = userRepository.find(jwtUserDetails.getId());
        userForUpdate.setEmail(newEmailDto.getNewEmail());
        userRepository.update(userForUpdate);
    }

    private User getUserById(Long id) {
        User user = userRepository.find(id);
        if (user == null) {
            log.warn("User {} is not found", id);
            throw new UserNotFoundException(id);
        }
        return user;
    }
}
