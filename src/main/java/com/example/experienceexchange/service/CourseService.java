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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
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

    @Transactional(readOnly = true)
    @Override
    public List<CourseDto> getCourses(List<SearchCriteria> filters) {
        log.debug("Get courses by filter");
        Map<String, List<SearchCriteria>> searchMap = filterProvider.createSearchMap(filters);
        String filterQuery = filterProvider.createFilterQuery(searchMap);

        List<Course> courses = courseRepository.findAllCoursesByFilter(filterQuery);
        return courseMapper.toCourseDto(courses);
    }

    @Transactional
    @Override
    public CourseDto createCourse(JwtUserDetails userDetails, CourseDto courseDto) {
        log.debug("Create course");
        Long userId = userDetails.getId();
        User author = userRepository.find(userId);
        Course newCourse = courseMapper.courseDtoToCourse(courseDto);
        newCourse.setAuthor(author);
        if (newCourse.getLessons() != null) {
            newCourse.getLessons()
                    .forEach(lessonOnCourse -> lessonOnCourse.setCourse(newCourse));
        }
        courseRepository.save(newCourse);
        courseRepository.update(newCourse);
        log.debug("Created course {}", newCourse.getId());
        return courseMapper.courseToCourseDto(newCourse);
    }

    @Transactional
    @Override
    public CourseDto editCourse(JwtUserDetails userDetails, Long courseId, CourseDto courseDto) {
        log.debug("Edit course {}", courseId);
        Course courseBeforeUpdate = getCourseById(courseId);

        checkAccessToCourseEdit(courseBeforeUpdate, userDetails.getId());

        Course courseAfterUpdate = courseMapper.courseDtoToCourse(courseDto);
        updateCourse(courseBeforeUpdate, courseAfterUpdate);

        courseRepository.update(courseAfterUpdate);  // TODO : затестить
        courseAfterUpdate
                .getLessons()
                .forEach(lesson -> lesson.setCourse(courseAfterUpdate));
        log.debug("Updated course {}", courseId);
        return courseMapper.courseToCourseDto(courseAfterUpdate);
    }

    @Transactional(readOnly = true)
    @Override
    public CourseDto getCourse(Long courseId) {
        log.debug("Get course {}", courseId);
        Course course = getCourseById(courseId);
        return courseMapper.courseToCourseDto(course);
    }

    @Transactional(readOnly = true)
    @Override
    public List<LessonOnCourseDto> getSchedule(JwtUserDetails userDetails) {
        log.debug("Get schedule of courses for user {}", userDetails.getId());
        Long userId = userDetails.getId();
        List<LessonOnCourse> allLessonsOnCourseByUserId = lessonOnCourseRepository.findAllLessonsOnCourseByUserId(userId);
        return lessonMapper.toLessonOnCourseDto(allLessonsOnCourseByUserId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<LessonOnCourseDto> getScheduleByCourse(JwtUserDetails userDetails, Long courseId) {
        log.debug("Get schedule of course {}", courseId);
        Long userId = userDetails.getId();
        List<LessonOnCourse> lessons = lessonOnCourseRepository.findAllLessonsOnCourseByUserIdAndCourseId(userId, courseId);
        return lessonMapper.toLessonOnCourseDto(lessons);
    }

    // TODO : TEST
    @Transactional(readOnly = true)
    @Override
    public LessonOnCourseDto getLessonOnCourse(JwtUserDetails userDetails, Long courseId, Long lessonId) {
        log.debug("Get lesson {} on course {}", lessonId, courseId);

        List<LessonOnCourse> lessons = lessonOnCourseRepository.findAllLessonsOnCourseByUserIdAndCourseId(userDetails.getId(), courseId);
        if (lessons.isEmpty()) {
            log.warn("No access to course {} for user {}", courseId, userDetails.getId());
            throw new NotAccessException(NOT_ACCESS_TO_COURSE);
        }
        LessonOnCourse lessonOnCourse = lessons
                .stream()
                .filter(lesson -> lesson.getId().equals(lessonId))
                .findFirst()
                .orElse(null);

        if (lessonOnCourse == null) {
            log.warn("Lesson {} is not found", lessonId);
            throw new LessonNotFoundException(lessonId);
        }

        checkAccessToLessonUse(lessonOnCourse);
        return lessonMapper.lessonOnCourseToLessonOnCourseDto(lessonOnCourse);
    }

    @Transactional
    @Override
    public void deleteCourse(JwtUserDetails userDetails, Long courseId) {
        log.debug("Delete {} course", courseId);
        Course course = getCourseById(courseId);
        checkAccessToCourseEdit(course, userDetails.getId());
        try {
            courseRepository.delete(course);
            log.debug("Course {} deleted", courseId);
        } catch (EntityExistsException e) {
            log.warn("Course {} is not found", courseId);
            throw new CourseNotFoundException(courseId);
        }
    }

    @Transactional
    @Override
    public PaymentDto subscribeToCourse(JwtUserDetails userDetails, PaymentDto paymentDto, Long courseId) {
        log.debug("User {} tries to subscribe to course {}", userDetails.getId(), courseId);
        Course course = getCourseById(courseId);

        User user = userRepository.find(userDetails.getId());
        if (course.getAuthor().getId().equals(user.getId())) {
            log.warn("User {} cannot subscribe to own course", userDetails.getId());
            throw new SubscriptionNotPossibleException(SUB_TO_OWN_COURSE);
        }

        if (course.isSatisfactoryPrice(paymentDto.getPrice())) {
            if (course.isAvailableForSubscription(DateUtil.dateTimeNow())) {
                Payment payment = subscribeToCourse(paymentDto, user, course);
                paymentRepository.save(payment);
                log.debug("User {} subscribe to course {}", userDetails.getId(), courseId);
                return paymentMapper.paymentToPaymentDto(payment);
            } else {
                log.warn("User {} cannot subscribe to close course", userDetails.getId());
                throw new SubscriptionNotPossibleException(SUB_TO_CLOSE_COURSE);
            }
        } else {
            log.warn("User {} cannot subscribe to course with incorrect price {}", userDetails.getId(), paymentDto.getPrice());
            throw new SubscriptionNotPossibleException(SUB_WITH_INCORRECT_PRICE);
        }
    }

    @Transactional
    @Override
    public CourseDto createLessonOnCourse(JwtUserDetails userDetails, Long courseId, LessonOnCourseDto lessonDto) {
        log.debug("Create lesson on course {}", courseId);
        Course course = getCourseById(courseId);

        checkAccessToCourseEdit(course, userDetails.getId());

        LessonOnCourse lessonOnCourse = lessonMapper.lessonOnCourseDtoToLessonOnCourse(lessonDto);
        lessonOnCourse.setCourse(course);
        course.addLesson(lessonOnCourse);

        lessonOnCourseRepository.save(lessonOnCourse);
        log.debug("Created lesson {} on course {}", lessonOnCourse.getId(), course.getId());
        return courseMapper.courseToCourseDto(course);
    }

    private Course getCourseById(Long id) throws CourseNotFoundException {
        Course course = courseRepository.find(id);
        if (course == null) {
            log.warn("Course {} is not found", id);
            throw new CourseNotFoundException(id);
        }
        return course;
    }

    private void checkAccessToCourseEdit(Course course, Long userId) throws NotAccessException {
        if (!course.getAuthor().getId().equals(userId)) {
            log.warn("User {} does not have access to edit course {}", userId, course.getId());
            throw new NotAccessException(NOT_ACCESS_EDIT);
        }
    }

    private Course updateCourse(Course courseBeforeUpdate, Course courseAfterUpdate) {
        log.trace("Updating course {}", courseBeforeUpdate.getId());
        courseAfterUpdate.setAuthor(courseBeforeUpdate.getAuthor());
        courseAfterUpdate.setCurrentNumberUsers(courseBeforeUpdate.getCurrentNumberUsers());
        courseAfterUpdate.setComments(courseBeforeUpdate.getComments());
        courseAfterUpdate.setUsersInCourse(courseBeforeUpdate.getUsersInCourse());
        return courseAfterUpdate;
    }

    private void checkAccessToLessonUse(LessonOnCourse lesson) throws NotAccessException {
        log.trace("Check if the lesson {} is available for use", lesson.getId());
        Date dateStartLesson = lesson.getStartLesson();
        Date dateAccessLesson = DateUtil.addDays(dateStartLesson, lesson.getAccessDuration());
        if (DateUtil.isDateAfterNow(dateStartLesson) && DateUtil.isDateBeforeNow(dateAccessLesson)) {
            log.warn("Lesson {} not available", lesson.getId());
            throw new NotAccessException(NOT_ACCESS_TO_LESSON);
        }
    }

    private Payment subscribeToCourse(PaymentDto paymentDto, User user, Course course) {
        Payment newPayment = paymentMapper.paymentDtoToPayment(paymentDto);
        newPayment.setDatePayment(DateUtil.dateTimeNow());

        user.addCourse(course);
        user.addPayment(newPayment);

        newPayment.setCourse(course);
        newPayment.setCostumer(user);

        course.increaseNumberSubscriptions();
        return newPayment;
    }
}
