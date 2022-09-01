package com.example.experienceexchange.service;

import com.example.experienceexchange.dto.CommentDto;
import com.example.experienceexchange.dto.LessonDto;
import com.example.experienceexchange.exception.LessonNotFoundException;
import com.example.experienceexchange.exception.NotAccessException;
import com.example.experienceexchange.model.Comment;
import com.example.experienceexchange.model.LessonSingle;
import com.example.experienceexchange.model.User;
import com.example.experienceexchange.repository.interfaceRepo.ICommentRepository;
import com.example.experienceexchange.repository.interfaceRepo.ILessonRepository;
import com.example.experienceexchange.repository.interfaceRepo.IUserRepository;
import com.example.experienceexchange.security.JwtUserDetails;
import com.example.experienceexchange.service.interfaceService.ILessonService;
import com.example.experienceexchange.util.date.DateUtil;
import com.example.experienceexchange.util.mapper.CommentMapper;
import com.example.experienceexchange.util.mapper.LessonMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Set;

@Service
public class LessonService implements ILessonService {

    private final ILessonRepository lessonRepository;
    private final IUserRepository userRepository;
    private final ICommentRepository commentRepository;
    private final LessonMapper lessonMapper;
    private final CommentMapper commentMapper;// сделать отдельный котроллер для комментов

    public LessonService(ILessonRepository lessonRepository, IUserRepository userRepository, ICommentRepository commentRepository, LessonMapper lessonMapper, CommentMapper commentMapper) {
        this.lessonRepository = lessonRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.lessonMapper = lessonMapper;
        this.commentMapper = commentMapper;
    }

    @Transactional
    @Override
    public LessonDto createLesson(JwtUserDetails userDetails, LessonDto lessonDto) {
        Long userId = userDetails.getId();
        User user = userRepository.find(userId);
        LessonSingle newLesson = lessonMapper.lessonDtoToLesson(lessonDto);
        newLesson.setAuthor(user);
        lessonRepository.save(newLesson);
        LessonSingle update = lessonRepository.update(newLesson); // надо ли так созхранять ?
        return lessonMapper.lessonToLessonDto(update);

    }

    @Transactional
    @Override
    public LessonDto editLesson(JwtUserDetails userDetails, Long id, LessonDto lessonDto) {
        LessonSingle oldLesson = getLessonById(id);
        checkAccessToLessonEdit(oldLesson.getAuthor().getId(), userDetails.getId());
        LessonSingle updateLesson = lessonMapper.lessonDtoToLesson(lessonDto);
        updateLesson.setAuthor(oldLesson.getAuthor());
        updateLesson.setCurrentNumberUsers(oldLesson.getCurrentNumberUsers());
        updateLesson.setComments(oldLesson.getComments());
        updateLesson.setUsersInLesson(oldLesson.getUsersInLesson());
        LessonSingle update = lessonRepository.update(updateLesson);
        return lessonMapper.lessonToLessonDto(update);
    }

    @Transactional
    @Override
    public void deleteLesson(JwtUserDetails userDetails, Long id) {
        try {
            lessonRepository.deleteById(id);
        } catch (EntityExistsException e) {
            throw new LessonNotFoundException(id);
        }
    }

    @Transactional
    @Override
    public List<LessonDto> getLessonByDirection() {
        List<LessonSingle> lessons = lessonRepository.findAll();
        return lessonMapper.toLessonDto(lessons);
    }

    @Override
    public void subscribeToLesson(Long id, JwtUserDetails userDetails) {

    }

    @Transactional
    @Override
    public CommentDto createComment(JwtUserDetails userDetails, Long lessonId, CommentDto commentDto) {
        LessonSingle lesson = getLessonById(lessonId);
        User user = userRepository.find(userDetails.getId());
        Comment comment = commentMapper.commentDtoToComment(commentDto);
        comment.setCreated(DateUtil.dateTimeNow());
        comment.setAuthor(user);
        comment.setLesson(lesson);
        lesson.addComment(comment);
        Comment save = commentRepository.save(comment);
        return commentMapper.commentToCommentDto(save);
    }

    @Transactional
    @Override
    public LessonDto getLesson(Long lessonId) {
        LessonSingle lesson = getLessonById(lessonId);
        return lessonMapper.lessonToLessonDto(lesson);
    }

    @Transactional
    @Override
    public List<CommentDto> getCommentByLesson(Long lessonId) {
        LessonSingle lesson = getLessonById(lessonId);
        Set<Comment> comments = lesson.getComments();
        return commentMapper.toCommentsDto(comments);
    }

    private LessonSingle getLessonById(Long lessonId) throws LessonNotFoundException {
        LessonSingle lesson = lessonRepository.find(lessonId);
        if (lesson == null) {
            throw new LessonNotFoundException(lessonId);
        }
        return lesson;
    }

    private void checkAccessToLessonEdit(Long authorId, Long authUserID) {
        if (!authorId.equals(authorId)) {
            throw new NotAccessException();
        }
    }
}
