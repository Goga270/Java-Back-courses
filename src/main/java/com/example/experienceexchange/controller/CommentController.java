package com.example.experienceexchange.controller;

import com.example.experienceexchange.dto.CommentDto;
import com.example.experienceexchange.security.JwtUserDetails;
import com.example.experienceexchange.service.interfaceService.ICommentService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class CommentController {

    private final ICommentService commentService;

    public CommentController(ICommentService commentService) {
        this.commentService = commentService;
    }

    @JsonView({CommentDto.CreateForCourse.class})
    @GetMapping("courses/{id}/comments")
    public List<CommentDto> getCommentsByCourse(@PathVariable("id") Long courseId) {
        return commentService.getCommentsByCourse(courseId);
    }

    @JsonView({CommentDto.CreateForLesson.class})
    @GetMapping("lessons/{id}/comments")
    public List<CommentDto> getCommentsByLesson(@PathVariable("id") Long lessonId) {
        return commentService.getCommentByLesson(lessonId);
    }

    @JsonView({CommentDto.CreateForCourse.class})
    @PostMapping("courses/new-comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createCommentForCourse(
            @AuthenticationPrincipal JwtUserDetails userDetails,
            @RequestBody @Validated(CommentDto.CreateForCourse.class) CommentDto commentDto) {
        return commentService.createCommentForCourse(userDetails, commentDto);
    }

    @JsonView({CommentDto.CreateForLesson.class})
    @PostMapping("lessons/new-comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createCommentForLesson(
            @AuthenticationPrincipal JwtUserDetails userDetails,
            @RequestBody @Validated(CommentDto.CreateForLesson.class) CommentDto commentDto) {
        return commentService.createCommentForLesson(userDetails, commentDto);
    }
}
