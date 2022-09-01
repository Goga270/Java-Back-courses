package com.example.experienceexchange.service;

import com.example.experienceexchange.dto.CommentDto;
import com.example.experienceexchange.dto.CourseDto;
import com.example.experienceexchange.dto.LessonOnCourseDto;
import com.example.experienceexchange.exception.CourseNotFoundException;
import com.example.experienceexchange.exception.NotAccessException;
import com.example.experienceexchange.model.Comment;
import com.example.experienceexchange.model.Course;
import com.example.experienceexchange.model.LessonOnCourse;
import com.example.experienceexchange.model.User;
import com.example.experienceexchange.repository.interfaceRepo.ICommentRepository;
import com.example.experienceexchange.repository.interfaceRepo.ICourseRepository;
import com.example.experienceexchange.repository.interfaceRepo.ILessonOnCourseRepository;
import com.example.experienceexchange.repository.interfaceRepo.IUserRepository;
import com.example.experienceexchange.security.JwtUserDetails;
import com.example.experienceexchange.service.interfaceService.ICourseService;
import com.example.experienceexchange.util.date.DateUtil;
import com.example.experienceexchange.util.mapper.CommentMapper;
import com.example.experienceexchange.util.mapper.CourseMapper;
import com.example.experienceexchange.util.mapper.LessonOnCourseMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Set;

@Service
public class CourseService implements ICourseService {

    private final ICourseRepository courseRepository;
    private final IUserRepository userRepository; // удалить ?
    private final ICommentRepository commentRepository;
    private final ILessonOnCourseRepository lessonOnCourseRepository;
    private final CommentMapper commentMapper;
    private final CourseMapper courseMapper;
    private final LessonOnCourseMapper lessonMapper;

    public CourseService(ICourseRepository courseRepository,
                         IUserRepository userRepository,
                         ICommentRepository commentRepository, ILessonOnCourseRepository lessonOnCourseRepository, CommentMapper commentMapper,
                         CourseMapper courseMapper, LessonOnCourseMapper lessonMapper) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.lessonOnCourseRepository = lessonOnCourseRepository;
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
    public void subscribeToCourse(Long id, JwtUserDetails userDetails) {

    }

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
