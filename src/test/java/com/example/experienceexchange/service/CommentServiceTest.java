package com.example.experienceexchange.service;

import com.example.experienceexchange.dto.CommentDto;
import com.example.experienceexchange.dto.CourseDto;
import com.example.experienceexchange.dto.LessonDto;
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
import com.example.experienceexchange.util.date.DateUtil;
import com.example.experienceexchange.util.mapper.CommentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentServiceTest {

    private static final Long COURSE_ID = 1L;
    private static final Long LESSON_ID = 2L;
    private static final Long USER_ID = 3L;

    @InjectMocks
    private CommentService commentService;
    @Mock
    private ICommentRepository commentRepository;
    @Mock
    private IUserRepository userRepository;
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private DateUtil dateUtil;
    @Mock
    private ILessonRepository lessonRepository;
    @Mock
    private ICourseRepository courseRepository;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_returnListCommentsDto_when_courseIdEntered() {
        List<Comment> comments = mock(List.class);
        List<CommentDto> expectedComments = mock(List.class);
        Course course = mock(Course.class);
        when(courseRepository.find(COURSE_ID)).thenReturn(course);
        when(commentRepository.findAllCommentsByCourseId(COURSE_ID)).thenReturn(comments);
        when(commentMapper.toCommentsDto(comments)).thenReturn(expectedComments);

        List<CommentDto> actual = commentService.getCommentsByCourse(COURSE_ID);

        verify(commentRepository).findAllCommentsByCourseId(COURSE_ID);
        assertNotNull(actual);
        assertEquals(expectedComments, actual);
    }

    @Test
    void should_throwCourseNotFoundException_when_courseIdIncorrect() {
        CourseDto courseDto = mock(CourseDto.class);
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        CommentDto enteredDto = mock(CommentDto.class);
        when(enteredDto.getCourse()).thenReturn(courseDto);
        when(courseDto.getId()).thenReturn(COURSE_ID);
        when(courseRepository.find(COURSE_ID)).thenReturn(null);

        CourseNotFoundException exceptionInGetComment = assertThrows(CourseNotFoundException.class, () -> commentService.getCommentsByCourse(COURSE_ID));
        CourseNotFoundException exceptionInCreateComment = assertThrows(CourseNotFoundException.class, () -> commentService.createCommentForCourse(userDetails,enteredDto));

        verify(courseRepository, times(2)).find(COURSE_ID);
        assertEquals(String.format("Course with id %d not found", COURSE_ID),exceptionInGetComment.getMessage());
        assertEquals(String.format("Course with id %d not found", COURSE_ID),exceptionInCreateComment.getMessage());
    }

    @Test
    void should_returnListCommentsDto_when_LessonIdEntered() {
        List<Comment> comments = mock(List.class);
        List<CommentDto> expectedComments = mock(List.class);
        LessonSingle lesson = mock(LessonSingle.class);
        when(commentRepository.findAllCommentsByLessonId(LESSON_ID)).thenReturn(comments);
        when(commentMapper.toCommentsDto(comments)).thenReturn(expectedComments);
        when(lessonRepository.find(LESSON_ID)).thenReturn(lesson);

        List<CommentDto> actual = commentService.getCommentByLesson(LESSON_ID);

        verify(commentRepository).findAllCommentsByLessonId(LESSON_ID);
        assertNotNull(actual);
        assertEquals(expectedComments, actual);
    }

    @Test
    void should_throwLessonNotFoundException_when_lessonIdIncorrect() {
        CommentDto enteredDto = mock(CommentDto.class);
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        LessonDto lessonDto = mock(LessonDto.class);
        when(enteredDto.getLesson()).thenReturn(lessonDto);
        when(lessonDto.getId()).thenReturn(LESSON_ID);
        when(lessonRepository.find(LESSON_ID)).thenReturn(null);

        LessonNotFoundException exceptionInGetComment = assertThrows(LessonNotFoundException.class, () -> commentService.getCommentByLesson(LESSON_ID));
        LessonNotFoundException exceptionInCreateComment = assertThrows(LessonNotFoundException.class, () -> commentService.createCommentForLesson(userDetails,enteredDto));

        verify(lessonRepository, times(2)).find(LESSON_ID);
        assertEquals(String.format("Lesson with id %d not found", LESSON_ID),exceptionInGetComment.getMessage());
        assertEquals(String.format("Lesson with id %d not found", LESSON_ID),exceptionInCreateComment.getMessage());
    }


    @Test
    void should_saveNewCommentInDataBase_if_enteredIdCourseCorrect() {
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        CommentDto enteredDto = mock(CommentDto.class);
        CommentDto expectedDto = mock(CommentDto.class);
        Comment newComment = mock(Comment.class);
        Date dateNow = Date.from(Instant.now());
        User author = mock(User.class);
        CourseDto courseDto = mock(CourseDto.class);
        Course course = mock(Course.class);
        when(enteredDto.getCourse()).thenReturn(courseDto);
        when(courseDto.getId()).thenReturn(COURSE_ID);
        when(commentMapper.commentDtoToComment(enteredDto)).thenReturn(newComment);
        when(userDetails.getId()).thenReturn(USER_ID);
        when(userRepository.find(USER_ID)).thenReturn(author);
        when(dateUtil.dateTimeNow()).thenReturn(dateNow);
        when(commentRepository.save(newComment)).thenReturn(newComment);
        when(courseRepository.find(COURSE_ID)).thenReturn(course);
        when(commentMapper.commentToCommentDto(newComment)).thenReturn(expectedDto);

        CommentDto actual = commentService.createCommentForCourse(userDetails, enteredDto);

        assertNotNull(actual);
        assertEquals(expectedDto,actual);
        verify(newComment).setCreated(dateNow);
        verify(commentRepository).save(newComment);
        verify(courseRepository).find(COURSE_ID);
    }

    @Test
    void should_saveNewCommentInDataBase_if_enteredIdLessonCorrect() {
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        CommentDto enteredDto = mock(CommentDto.class);
        CommentDto expectedDto = mock(CommentDto.class);
        Comment newComment = mock(Comment.class);
        Date dateNow = Date.from(Instant.now());
        User author = mock(User.class);
        LessonDto lessonDto = mock(LessonDto.class);
        LessonSingle lessonSingle = mock(LessonSingle.class);
        when(enteredDto.getLesson()).thenReturn(lessonDto);
        when(lessonDto.getId()).thenReturn(LESSON_ID);
        when(commentMapper.commentDtoToComment(enteredDto)).thenReturn(newComment);
        when(userDetails.getId()).thenReturn(USER_ID);
        when(userRepository.find(USER_ID)).thenReturn(author);
        when(dateUtil.dateTimeNow()).thenReturn(dateNow);
        when(commentRepository.save(newComment)).thenReturn(newComment);
        when(lessonRepository.find(LESSON_ID)).thenReturn(lessonSingle);
        when(commentMapper.commentToCommentDto(newComment)).thenReturn(expectedDto);

        CommentDto actual = commentService.createCommentForLesson(userDetails, enteredDto);

        assertNotNull(actual);
        assertEquals(expectedDto,actual);
        verify(newComment).setCreated(dateNow);
        verify(commentRepository).save(newComment);
        verify(lessonRepository).find(LESSON_ID);
    }
}