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


/**
 * Работа с комментариями
 */
@RestController
public class CommentController {

    private final ICommentService commentService;

    public CommentController(ICommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Достать комментарии под курсом
     * @param courseId Идентификатор курса
     * @return Массив комментариев
     */
    @JsonView({CommentDto.CreateForCourse.class})
    @GetMapping("courses/{id}/comments")
    public List<CommentDto> getCommentsByCourse(@PathVariable("id") Long courseId) {
        return commentService.getCommentsByCourse(courseId);
    }

    /**
     * Достать комментарии под уроком
     * @param lessonId Идентификатор урока
     * @return Массив комментариев
     */
    @JsonView({CommentDto.CreateForLesson.class})
    @GetMapping("lessons/{id}/comments")
    public List<CommentDto> getCommentsByLesson(@PathVariable("id") Long lessonId) {
        return commentService.getCommentByLesson(lessonId);
    }

    /**
     * Создать новый комментарий для курса
     * @param userDetails Данные пользователя
     * @param commentDto Данные комментария
     * @return Новый комментарий
     */
    @JsonView({CommentDto.CreateForCourse.class})
    @PostMapping("courses/new-comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createCommentForCourse(
            @AuthenticationPrincipal JwtUserDetails userDetails,
            @RequestBody @Validated(CommentDto.CreateForCourse.class) CommentDto commentDto) {
        return commentService.createCommentForCourse(userDetails, commentDto);
    }

    /**
     *  Создать новый комментарий для урока
     * @param userDetails Данные пользователя
     * @param commentDto Данные комментария
     * @return Новый комментарий
     */
    @JsonView({CommentDto.CreateForLesson.class})
    @PostMapping("lessons/new-comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createCommentForLesson(
            @AuthenticationPrincipal JwtUserDetails userDetails,
            @RequestBody @Validated(CommentDto.CreateForLesson.class) CommentDto commentDto) {
        return commentService.createCommentForLesson(userDetails, commentDto);
    }
}
