package com.example.experienceexchange.controller;

import com.example.experienceexchange.dto.*;
import com.example.experienceexchange.security.JwtUserDetails;
import com.example.experienceexchange.service.interfaceService.ILessonService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.logging.Filter;

@RestController
@RequestMapping("/lessons")
public class LessonController {

    private final ILessonService lessonService;

    public LessonController(ILessonService lessonService) {
        this.lessonService = lessonService;
    }

    /*@GetMapping({"/{direction}"})
    public Set<LessonDto> getLessonByDirection(@PathVariable String direction,
                                                @RequestBody FilterDto filterDto) {
        return lessonService.getLessonByDirection();
    }*/

    @JsonView({LessonDto.Details.class})
    @GetMapping("")
    public List<LessonDto> getLessons(@RequestBody FilterDto filterDto) {
        return lessonService.getLessonByDirection();
    }

    @GetMapping("/{id}")
    public LessonDto getLesson(@PathVariable("id") Long lessonId) {
        return lessonService.getLesson(lessonId);
    }


    @GetMapping("/{id}/comments")
    public List<CommentDto> getCommentsByLesson(@PathVariable("id") Long lessonId) {
        return lessonService.getCommentByLesson(lessonId);
    }

    @PostMapping("/new-lesson")
    @ResponseStatus(HttpStatus.CREATED)
    public LessonDto createLesson(@AuthenticationPrincipal JwtUserDetails userDetails,
                                  @RequestBody @Validated(LessonDto.Create.class) LessonDto lessonDto) {
        return lessonService.createLesson(userDetails, lessonDto);
    }

    @JsonView({PaymentDto.DetailsForPayLesson.class})
    @PostMapping("/{id}/subscribe")
    @ResponseStatus(HttpStatus.OK)
    public PaymentDto subscribeToLesson(@AuthenticationPrincipal JwtUserDetails userDetails,
                                        @PathVariable("id") Long lessonId,
                                        @RequestBody @Validated(PaymentDto.Create.class) PaymentDto paymentDto) {
        return lessonService.subscribeToLesson(userDetails, paymentDto, lessonId);
    }

    @PostMapping("/{id}/new-comment")
    public CommentDto createComment(
            @PathVariable("id") Long lessonId,
            @AuthenticationPrincipal JwtUserDetails userDetails,
            @RequestBody @Validated(CommentDto.Create.class) CommentDto commentDto) {
        return lessonService.createComment(userDetails, lessonId, commentDto);
    }

    @PutMapping("/{id}/settings")
    public LessonDto editLesson(@AuthenticationPrincipal JwtUserDetails userDetails,
                                @PathVariable Long id,
                                @RequestBody @Validated(LessonDto.Edit.class) LessonDto lessonDto) {
        return lessonService.editLesson(userDetails, id, lessonDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLesson(@AuthenticationPrincipal JwtUserDetails userDetails,
                             @PathVariable Long id) {
        lessonService.deleteLesson(userDetails, id);
    }
}
