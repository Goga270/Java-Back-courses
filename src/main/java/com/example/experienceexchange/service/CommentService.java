package com.example.experienceexchange.service;

import com.example.experienceexchange.dto.CommentDto;
import com.example.experienceexchange.exception.CourseNotFoundException;
import com.example.experienceexchange.exception.LessonNotFoundException;
import com.example.experienceexchange.model.Comment;
import com.example.experienceexchange.model.Course;
import com.example.experienceexchange.model.LessonSingle;
import com.example.experienceexchange.model.User;
import com.example.experienceexchange.repository.interfaceRepo.ICommentRepository;
import com.example.experienceexchange.repository.interfaceRepo.ICourseRepository;
import com.example.experienceexchange.repository.interfaceRepo.ILessonRepository;
import com.example.experienceexchange.repository.interfaceRepo.IUserRepository;
import com.example.experienceexchange.security.JwtUserDetails;
import com.example.experienceexchange.service.interfaceService.ICommentService;
import com.example.experienceexchange.util.date.DateUtil;
import com.example.experienceexchange.util.mapper.CommentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class CommentService implements ICommentService {

    private final ICommentRepository commentRepository;
    private final ILessonRepository lessonRepository;
    private final ICourseRepository courseRepository;
    private final IUserRepository userRepository;
    private final CommentMapper commentMapper;
    private final DateUtil dateUtil;

    public CommentService(ICommentRepository commentRepository,
                          ILessonRepository lessonRepository,
                          ICourseRepository courseRepository,
                          IUserRepository userRepository,
                          CommentMapper commentMapper,
                          DateUtil dateUtil) {
        this.commentRepository = commentRepository;
        this.lessonRepository = lessonRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.commentMapper = commentMapper;
        this.dateUtil = dateUtil;
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> getCommentsByCourse(Long courseId) {
        log.debug("Get comments for course {}", courseId);
        isExistCourse(courseId);
        List<Comment> allCommentsByCourseId = commentRepository.findAllCommentsByCourseId(courseId);
        return commentMapper.toCommentsDto(allCommentsByCourseId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> getCommentByLesson(Long lessonId) {
        log.debug("Get comments for lesson {}", lessonId);
        isExistLesson(lessonId);
        List<Comment> allCommentsByLessonId = commentRepository.findAllCommentsByLessonId(lessonId);
        return commentMapper.toCommentsDto(allCommentsByLessonId);
    }

    @Transactional
    @Override
    public CommentDto createCommentForCourse(JwtUserDetails userDetails, CommentDto commentDto) {
        log.debug("Create new comment");
        isExistCourse(commentDto.getCourse().getId());
        return createComment(userDetails, commentDto);
    }

    @Transactional
    @Override
    public CommentDto createCommentForLesson(JwtUserDetails userDetails, CommentDto commentDto) {
        log.debug("Create new comment");
        isExistLesson(commentDto.getLesson().getId());
        return createComment(userDetails, commentDto);
    }

    private CommentDto createComment(JwtUserDetails userDetails, CommentDto commentDto) {
        Comment comment = commentMapper.commentDtoToComment(commentDto);
        User authorComment = userRepository.find(userDetails.getId());
        comment.setCreated(dateUtil.dateTimeNow());
        comment.setAuthor(authorComment);
        Comment save = commentRepository.save(comment);
        log.debug("Created new comment {}", save.getId());
        return commentMapper.commentToCommentDto(save);
    }

    private Course isExistCourse(Long id) throws CourseNotFoundException {
        Course course = courseRepository.find(id);
        if (course == null) {
            log.warn("Course {} is not found", id);
            throw new CourseNotFoundException(id);
        }
        return course;
    }

    private LessonSingle isExistLesson(Long lessonId) throws LessonNotFoundException {
        LessonSingle lesson = lessonRepository.find(lessonId);
        if (lesson == null) {
            log.warn("Lesson {} not found", lessonId);
            throw new LessonNotFoundException(lessonId);
        }
        return lesson;
    }
}
