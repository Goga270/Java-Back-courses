package com.example.experienceexchange.service;

import com.example.experienceexchange.dto.CourseDto;
import com.example.experienceexchange.dto.LessonOnCourseDto;
import com.example.experienceexchange.dto.PaymentDto;
import com.example.experienceexchange.exception.CourseNotFoundException;
import com.example.experienceexchange.exception.LessonNotFoundException;
import com.example.experienceexchange.exception.NotAccessException;
import com.example.experienceexchange.exception.SubscriptionNotPossibleException;
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
import com.example.experienceexchange.service.interfaceService.ICourseService;
import com.example.experienceexchange.util.date.DateUtil;
import com.example.experienceexchange.util.mapper.CourseMapper;
import com.example.experienceexchange.util.mapper.LessonOnCourseMapper;
import com.example.experienceexchange.util.mapper.PaymentMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CourseService implements ICourseService {

    private static final String SUB_TO_OWN_COURSE = "Subscription to own course is not possible";
    private static final String SUB_TO_CLOSE_COURSE = "Course is closed for subscription";
    private static final String SUB_WITH_INCORRECT_PRICE = "Entered price is less than the fixed price";
    private static final String NOT_ACCESS_EDIT = "No access to edit resource";
    private static final String NOT_ACCESS_TO_COURSE = "no access to course";
    private static final String NOT_ACCESS_TO_LESSON = "no access to lesson on course";

    private final ICourseRepository courseRepository;
    private final IUserRepository userRepository;
    private final ILessonOnCourseRepository lessonOnCourseRepository;
    private final IPaymentRepository paymentRepository;
    private final IFilterProvider filterProvider;
    private final PaymentMapper paymentMapper;
    private final CourseMapper courseMapper;
    private final LessonOnCourseMapper lessonMapper;

    public CourseService(ICourseRepository courseRepository,
                         IUserRepository userRepository,
                         ILessonOnCourseRepository lessonOnCourseRepository,
                         IPaymentRepository paymentRepository,
                         @Qualifier("courseFilter") IFilterProvider filterProvider,
                         PaymentMapper paymentMapper,
                         CourseMapper courseMapper,
                         LessonOnCourseMapper lessonMapper) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.lessonOnCourseRepository = lessonOnCourseRepository;
        this.paymentRepository = paymentRepository;
        this.filterProvider = filterProvider;
        this.paymentMapper = paymentMapper;
        this.courseMapper = courseMapper;
        this.lessonMapper = lessonMapper;
    }

    @Transactional
    @Override
    public List<CourseDto> getCourses(List<SearchCriteria> filters) {
        Map<String, List<SearchCriteria>> searchMap = filterProvider.createSearchMap(filters);
        String filterQuery = filterProvider.createFilterQuery(searchMap);

        List<Course> courses = courseRepository.findAllCourseByFilter(filterQuery);
        return courseMapper.toCourseDto(courses);
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
    public List<LessonOnCourseDto> getSchedule(JwtUserDetails userDetails) {
        Long userId = userDetails.getId();
        List<LessonOnCourse> allLessonsOnCourseByUserId = lessonOnCourseRepository.findAllLessonsOnCourseByUserId(userId);
        return lessonMapper.toLessonOnCourseDto(allLessonsOnCourseByUserId);
    }

    @Transactional
    @Override
    public List<LessonOnCourseDto> getScheduleByCourse(JwtUserDetails userDetails, Long courseId) {
        Long userId = userDetails.getId();
        List<LessonOnCourse> lessons = lessonOnCourseRepository.findAllLessonsOnCourseByUserIdAndCourseId(userId, courseId);
        return lessonMapper.toLessonOnCourseDto(lessons);
    }

    // TODO : TEST
    @Override
    public LessonOnCourseDto getLessonOnCourse(JwtUserDetails userDetails, Long courseId, Long lessonId) {
        List<LessonOnCourse> lessons = lessonOnCourseRepository.findAllLessonsOnCourseByUserIdAndCourseId(userDetails.getId(), courseId);
        if (lessons.isEmpty()) {
            throw new NotAccessException(NOT_ACCESS_TO_COURSE);
        }
        LessonOnCourse lessonOnCourse = lessons
                .stream()
                .filter(lesson -> lesson.getId().equals(lessonId))
                .findFirst()
                .orElse(null);

        if (lessonOnCourse == null) {
            throw new LessonNotFoundException(lessonId);
        }
        // TODO : TEST
        Date dateStartLesson = lessonOnCourse.getStartLesson();
        Date dateAccessLesson = DateUtil.addDays(dateStartLesson, lessonOnCourse.getAccessDuration());
        if (DateUtil.isDateAfterNow(dateStartLesson) && DateUtil.isDateBeforeNow(dateAccessLesson)) {
            throw new NotAccessException(NOT_ACCESS_TO_LESSON);
        }
        return lessonMapper.lessonOnCourseToLessonOnCourseDto(lessonOnCourse);
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

    @Transactional
    @Override
    public CourseDto createLessonOnCourse(JwtUserDetails userDetails, Long courseId, LessonOnCourseDto lessonDto) {
        Course course = getCourseById(courseId);
        checkAccessToCourseEdit(course, userDetails.getId());
        LessonOnCourse lessonOnCourse = lessonMapper.lessonOnCourseDtoToLessonOnCourse(lessonDto);
        lessonOnCourse.setCourse(course);
        course.addLesson(lessonOnCourse);
        lessonOnCourseRepository.save(lessonOnCourse);
        return courseMapper.courseToCourseDto(course);
    }

    private Course getCourseById(Long id) throws CourseNotFoundException {
        Course course = courseRepository.find(id);
        if (course == null) {
            throw new CourseNotFoundException(id);
        }
        return course;
    }

    private void checkAccessToCourseEdit(Course course, Long userId) throws NotAccessException {
        if (!course.getAuthor().getId().equals(userId)) {
            throw new NotAccessException(NOT_ACCESS_EDIT);
        }
    }
}
