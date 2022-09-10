package com.example.experienceexchange.service;

import com.example.experienceexchange.dto.CommentDto;
import com.example.experienceexchange.model.Comment;
import com.example.experienceexchange.model.User;
import com.example.experienceexchange.repository.interfaceRepo.ICommentRepository;
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
    private final IUserRepository userRepository;
    private final CommentMapper commentMapper;

    public CommentService(ICommentRepository commentRepository,
                          IUserRepository userRepository,
                          CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.commentMapper = commentMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> getCommentsByCourse(Long courseId) {
        log.debug("Get comments for course id={}", courseId);
        List<Comment> allCommentsByCourseId = commentRepository.findAllCommentsByCourseId(courseId);
        return commentMapper.toCommentsDto(allCommentsByCourseId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> getCommentByLesson(Long lessonId) {
        log.debug("Get comments for lesson id={}", lessonId);
        List<Comment> allCommentsByLessonId = commentRepository.findAllCommentsByLessonId(lessonId);
        return commentMapper.toCommentsDto(allCommentsByLessonId);
    }

    @Transactional
    @Override
    public CommentDto createComment(JwtUserDetails userDetails, CommentDto commentDto) {
        log.debug("Create comment");
        Comment comment = commentMapper.commentDtoToComment(commentDto);
        User authorComment = userRepository.find(userDetails.getId());
        comment.setCreated(DateUtil.dateTimeNow());
        comment.setAuthor(authorComment);
        Comment save = commentRepository.save(comment);
        return commentMapper.commentToCommentDto(save);
    }
}
