package com.example.experienceexchange.service;

import com.example.experienceexchange.dto.CourseDto;
import com.example.experienceexchange.dto.LessonOnCourseDto;
import com.example.experienceexchange.dto.PaymentDto;
import com.example.experienceexchange.dto.SkillDto;
import com.example.experienceexchange.exception.*;
import com.example.experienceexchange.model.*;
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
import com.example.experienceexchange.util.mapper.SkillMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CourseService implements ICourseService {

    public static final String SUB_TO_OWN_COURSE = "Subscription to own course is not possible";
    public static final String SUB_TO_CLOSE_COURSE = "Course is closed for subscription";
    public static final String SUB_WITH_INCORRECT_PRICE = "Entered price is less than the fixed price";
    public static final String NOT_ACCESS_EDIT = "No access to edit resource";
    public static final String NOT_ACCESS_TO_LESSON = "no access to lesson on course";
    public static final String COURSE_NOT_END = "Course is not ended";
    public static final String ILLEGAL_FILTER = "Entered search criteria is incorrect";

    private final ICourseRepository courseRepository;
    private final IUserRepository userRepository;
    private final ILessonOnCourseRepository lessonOnCourseRepository;
    private final IPaymentRepository paymentRepository;
    private final IFilterProvider filterProvider;
    private final PaymentMapper paymentMapper;
    private final CourseMapper courseMapper;
    private final LessonOnCourseMapper lessonMapper;
    private final DateUtil dateUtil;
    private final SkillMapper skillMapper;

    public CourseService(ICourseRepository courseRepository,
                         IUserRepository userRepository,
                         ILessonOnCourseRepository lessonOnCourseRepository,
                         IPaymentRepository paymentRepository,
                         @Qualifier("courseFilter") IFilterProvider filterProvider,
                         PaymentMapper paymentMapper,
                         CourseMapper courseMapper,
                         LessonOnCourseMapper lessonMapper,
                         DateUtil dateUtil, SkillMapper skillMapper) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.lessonOnCourseRepository = lessonOnCourseRepository;
        this.paymentRepository = paymentRepository;
        this.filterProvider = filterProvider;
        this.paymentMapper = paymentMapper;
        this.courseMapper = courseMapper;
        this.lessonMapper = lessonMapper;
        this.dateUtil = dateUtil;
        this.skillMapper = skillMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public List<CourseDto> getCourses(List<SearchCriteria> filters) {
        log.debug("Get courses by filter");
        Map<String, List<SearchCriteria>> searchMap = filterProvider.createSearchMap(filters);
        String filterQuery = filterProvider.createFilterQuery(searchMap);
        try {
            List<Course> courses = courseRepository.findAllCoursesByFilter(filterQuery);
            return courseMapper.toCourseDto(courses);
        } catch (InvalidDataAccessResourceUsageException e) {
            log.warn("Incorrect filter {} for query", filterQuery);
            throw new IllegalSearchCriteriaException(ILLEGAL_FILTER);
        }
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
        List<LessonOnCourse> allLessonsOnCourseByUserId = lessonOnCourseRepository.findAllLessonsOnSubscribedCoursesByUserId(userId);
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

    @Transactional(readOnly = true)
    @Override
    public List<LessonOnCourseDto> getlessonsByCourse(Long courseId) {
        log.debug("Get lessons of course {}", courseId);
        List<LessonOnCourse> lessons = lessonOnCourseRepository.findAllLessonsOnCourse(courseId);
        return lessonMapper.toLessonOnCourseDto(lessons);
    }

    @Transactional(readOnly = true)
    @Override
    public List<SkillDto> getSkills() {
        return skillMapper.toSkillDto(courseRepository.getSkills());
    }

    @Transactional(readOnly = true)
    @Override
    public LessonOnCourseDto getLessonOnCourse(JwtUserDetails userDetails, Long courseId, Long lessonId) {
        log.debug("Get lesson {} on course {}", lessonId, courseId);

        LessonOnCourse lessonOnCourse = lessonOnCourseRepository.findLessonInCourseForSubscriber(userDetails.getId(), courseId, lessonId);

        if (lessonOnCourse == null) {
            log.warn("Lesson {} is not found", lessonId);
            throw new LessonNotFoundException(lessonId);
        }

        checkAccessToLessonUse(lessonOnCourse);
        return lessonMapper.lessonOnCourseToLessonOnCourseDto(lessonOnCourse);
    }

    @Transactional
    @Override
    public CourseDto restartCourse(JwtUserDetails userDetails, CourseDto courseDto) {
        log.debug("Restart course");
        User author = userRepository.find(userDetails.getId());
        Course courseToRestart = getCourseById(courseDto.getId());

        checkAccessToCourseEdit(courseToRestart, author.getId());

        if (dateUtil.isDateAfterNow(courseToRestart.getDateEnd())) {
            log.warn("Course not restarted because it hasn't finished");
            throw new NotAccessException(COURSE_NOT_END);
        }
        Course restartedCourse = courseMapper.courseDtoToCourse(courseDto);
        restartedCourse.setAuthor(author);
        restartedCourse.setCurrentNumberUsers(0);
        restartedCourse.setComments(courseToRestart.getComments());

        restartedCourse
                .getLessons()
                .forEach(lesson -> lesson.setCourse(restartedCourse));

        Course update = courseRepository.update(restartedCourse);
        log.debug("Restarted course {}", courseDto.getId());
        return courseMapper.courseToCourseDto(update);
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
        newCourse.setCurrentNumberUsers(0);

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

        courseAfterUpdate
                .getLessons()
                .forEach(lesson -> lesson.setCourse(courseAfterUpdate));
        Course update = courseRepository.update(courseAfterUpdate);
        log.debug("Updated course {}", courseId);
        return courseMapper.courseToCourseDto(update);
    }

    @Transactional
    @Override
    public void deleteCourse(JwtUserDetails userDetails, Long courseId) {
        log.debug("Delete {} course", courseId);
        Course course = getCourseById(courseId);
        checkAccessToCourseEdit(course, userDetails.getId());
        courseRepository.delete(course);
        log.debug("Course {} deleted", courseId);
    }

    @Transactional
    @Override
    public PaymentDto subscribeToCourse(JwtUserDetails userDetails, PaymentDto paymentDto, Long courseId) {
        log.debug("User {} tries to subscribe to course {}", userDetails.getId(), courseId);
        Course course = getCourseById(courseId);

        if (course.getAuthor().getId().equals(userDetails.getId())) {
            log.warn("User {} cannot subscribe to own course", userDetails.getId());
            throw new SubscriptionNotPossibleException(SUB_TO_OWN_COURSE);
        }
        User user = userRepository.find(userDetails.getId());

        if (course.isSatisfactoryPrice(paymentDto.getPrice())) {
            if (course.isAvailableForSubscription(dateUtil.dateTimeNow())) {
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
        Date dateAccessLesson = dateUtil.addDays(dateStartLesson, lesson.getAccessDuration());
        if (dateUtil.isDateAfterNow(dateStartLesson) || dateUtil.isDateBeforeNow(dateAccessLesson)) {
            log.warn("Lesson {} not available", lesson.getId());
            throw new NotAccessException(NOT_ACCESS_TO_LESSON);
        }
    }

    private Payment subscribeToCourse(PaymentDto paymentDto, User user, Course course) {
        Payment newPayment = paymentMapper.paymentDtoToPayment(paymentDto);
        newPayment.setDatePayment(dateUtil.dateTimeNow());

        course.getUsersInCourse().add(user);
        user.addPayment(newPayment);

        newPayment.setCourse(course);
        newPayment.setCostumer(user);

        course.increaseNumberSubscriptions();
        return newPayment;
    }
}
