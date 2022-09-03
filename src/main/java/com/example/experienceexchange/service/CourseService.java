package com.example.experienceexchange.service;

import com.example.experienceexchange.dto.CommentDto;
import com.example.experienceexchange.dto.CourseDto;
import com.example.experienceexchange.dto.LessonOnCourseDto;
import com.example.experienceexchange.dto.PaymentDto;
import com.example.experienceexchange.exception.CourseNotFoundException;
import com.example.experienceexchange.exception.NotAccessException;
import com.example.experienceexchange.exception.SubscriptionNotPossibleException;
import com.example.experienceexchange.model.*;
import com.example.experienceexchange.repository.interfaceRepo.*;
import com.example.experienceexchange.security.JwtUserDetails;
import com.example.experienceexchange.service.interfaceService.ICourseService;
import com.example.experienceexchange.util.date.DateUtil;
import com.example.experienceexchange.util.mapper.CommentMapper;
import com.example.experienceexchange.util.mapper.CourseMapper;
import com.example.experienceexchange.util.mapper.LessonOnCourseMapper;
import com.example.experienceexchange.util.mapper.PaymentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Set;

@Service
public class CourseService implements ICourseService {

    private static final String SUB_TO_OWN_COURSE = "Subscription to own course is not possible";
    private static final String SUB_TO_CLOSE_COURSE = "Course is closed for subscription";
    private static final String SUB_WITH_INCORRECT_PRICE = "Entered price is less than the fixed price";

    private final ICourseRepository courseRepository;
    private final IUserRepository userRepository; // удалить ?
    private final ICommentRepository commentRepository;
    private final ILessonOnCourseRepository lessonOnCourseRepository;
    private final IPaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final CommentMapper commentMapper;
    private final CourseMapper courseMapper;
    private final LessonOnCourseMapper lessonMapper;

    public CourseService(ICourseRepository courseRepository,
                         IUserRepository userRepository,
                         ICommentRepository commentRepository,
                         ILessonOnCourseRepository lessonOnCourseRepository,
                         IPaymentRepository paymentRepository,
                         PaymentMapper paymentMapper,
                         CommentMapper commentMapper,
                         CourseMapper courseMapper,
                         LessonOnCourseMapper lessonMapper) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.lessonOnCourseRepository = lessonOnCourseRepository;
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.commentMapper = commentMapper;
        this.courseMapper = courseMapper;
        this.lessonMapper = lessonMapper;
    }

    @Transactional
    @Override
    public List<CourseDto> getCoursesByDirection() {
        List<Course> all = courseRepository.findAll();
        return courseMapper.toCourseDto(all);
    }

    @Transactional
    @Override
    public CourseDto createCourse(JwtUserDetails userDetails, CourseDto courseDto) {
        Long userId = userDetails.getId();
        User user = userRepository.find(userId);
        Course newCourse = courseMapper.courseDtoToCourse(courseDto);
        newCourse.setAuthor(user);
        if (newCourse.getLessons() != null) {
            newCourse.getLessons()
                    .forEach(lessonOnCourse -> lessonOnCourse.setCourse(newCourse));
        }
        courseRepository.save(newCourse);
        courseRepository.update(newCourse);
        return courseMapper.courseToCourseDto(newCourse);
    }
    // TODO : ЕСЛИ НА КУРС УЖЕ ЕСТЬ ЗАПИСЬ ТО НВЕРНО ЧТО ТО МОЖНО МЕНЯТЬ
    // TODO : СРАВНИТЬ ID ИЗ PathVariable and courseDto
    @Transactional
    @Override
    public CourseDto editCourse(JwtUserDetails userDetails, Long id, CourseDto courseDto) {
        Course oldCourse = getCourseById(id);
        checkAccessToCourseEdit(oldCourse, userDetails.getId());
        Course updateCourse = courseMapper.courseDtoToCourse(courseDto);
        updateCourse.setAuthor(oldCourse.getAuthor());
        updateCourse.setCurrentNumberUsers(oldCourse.getCurrentNumberUsers());
        updateCourse.setComments(oldCourse.getComments());
        updateCourse.setUsersInCourse(oldCourse.getUsersInCourse());
        Course update = courseRepository.update(updateCourse);
        update.getLessons().forEach(lesson -> lesson.setCourse(update));
        return courseMapper.courseToCourseDto(update);
    }

    @Transactional
    @Override
    public CourseDto getCourse(Long courseId) {
        Course course = getCourseById(courseId);
        return courseMapper.courseToCourseDto(course);
    }

    @Transactional
    @Override
    public void deleteCourse(JwtUserDetails userDetails, Long id) {
        Course course = courseRepository.find(id);
        checkAccessToCourseEdit(course, userDetails.getId());
        try {
            courseRepository.delete(course);
        } catch (EntityExistsException e) {
            throw new CourseNotFoundException(id);
        }
    }

    @Transactional
    @Override
    public PaymentDto subscribeToCourse(JwtUserDetails userDetails, PaymentDto paymentDto, Long courseId) {
        Course course = getCourseById(courseId);
        User user = userRepository.find(userDetails.getId());
        if (course.getAuthor().getId().equals(user.getId())) {
            throw new SubscriptionNotPossibleException(SUB_TO_OWN_COURSE);
        }

        if (course.isSatisfactoryPrice(paymentDto.getPrice())) {
            if (course.isAvailableForSubscription(DateUtil.dateTimeNow())) {
                Payment payment = paymentMapper.paymentDtoToPayment(paymentDto);
                payment.setDatePayment(DateUtil.dateTimeNow());
                user.addCourse(course);
                user.addPayment(payment);
                payment.setCourse(course);
                payment.setCostumer(user);
                course.increaseNumberSubscriptions();
                paymentRepository.save(payment);
                return paymentMapper.paymentToPaymentDto(payment);
            } else {
                throw new SubscriptionNotPossibleException(SUB_TO_CLOSE_COURSE);
            }
        } else {
            throw new SubscriptionNotPossibleException(SUB_WITH_INCORRECT_PRICE);
        }
    }
    // TODO : ДОБАВИТЬ В FILL СКРИПТ CURRENT_NUMBER_USER, А ТО БУДЕТ ОШИБКА БУДЕТ NULLpOITER
    // TODO : УДАЛИТЬ ТАБУЛЯЦИЮ В DESCRIPTION
    @Transactional
    @Override
    public CommentDto createComment(Long courseId, JwtUserDetails userDetails, CommentDto commentDto) {
        Course course = getCourseById(courseId);
        User user = userRepository.find(userDetails.getId());
        Comment comment = commentMapper.commentDtoToComment(commentDto);
        comment.setCreated(DateUtil.dateTimeNow());
        comment.setAuthor(user);
        comment.setCourse(course);
        course.addComment(comment);
        Comment save = commentRepository.save(comment);
        CommentDto dto = commentMapper.commentToCommentDto(save);
        return dto;
    }

    @Transactional
    @Override
    public List<CommentDto> getCommentsByCourse(Long courseId) {
        Course course = getCourseById(courseId);
        Set<Comment> comments = course.getComments();
        return commentMapper.toCommentsDto(comments);

    }

    // TODO : ПРОВЕРИТЬ ДАТЫ ЧТОБЫ КОНЕЦ КУРСА БЫЛ ПОСЛЕ ЕГО НАЧАЛА  - ДЛЯ ЧАЙНИКОВ
    @Transactional
    @Override
    public CourseDto createLesson(JwtUserDetails userDetails, Long courseId, LessonOnCourseDto lessonDto) {
        Course course = getCourseById(courseId);
        checkAccessToCourseEdit(course, userDetails.getId());
        LessonOnCourse lessonOnCourse = lessonMapper.lessonOnCourseDtoToLessonOnCourse(lessonDto);
        lessonOnCourse.setCourse(course);
        course.addLesson(lessonOnCourse);
        lessonOnCourseRepository.save(lessonOnCourse);
        return courseMapper.courseToCourseDto(course);
    }

    private Course getCourseById(Long id) {
        Course course = courseRepository.find(id);
        if (course == null) {
            throw new CourseNotFoundException(id);
        }
        return course;
    }
    // TODO: 1 PAR - id author !!!!
    private void checkAccessToCourseEdit(Course course, Long userId) {
        if (!course.getAuthor().getId().equals(userId)) {
            throw new NotAccessException();
        }
    }
}
