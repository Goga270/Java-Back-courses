package com.example.experienceexchange.service.interfaceService;

import com.example.experienceexchange.dto.CommentDto;
import com.example.experienceexchange.security.JwtUserDetails;

import java.util.List;

public interface ICommentService {

    List<CommentDto> getCommentsByCourse(Long courseId);

    List<CommentDto> getCommentByLesson(Long lessonId);

    CommentDto createCommentForCourse(JwtUserDetails userDetails, CommentDto commentDto);

    CommentDto createCommentForLesson(JwtUserDetails userDetails, CommentDto commentDto);
}
