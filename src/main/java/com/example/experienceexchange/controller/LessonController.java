package com.example.experienceexchange.controller;

import com.example.experienceexchange.dto.CommentDto;
import com.example.experienceexchange.dto.CourseDto;
import com.example.experienceexchange.dto.FilterDto;
import com.example.experienceexchange.dto.LessonDto;
import com.example.experienceexchange.security.JwtUserDetails;
import com.example.experienceexchange.service.interfaceService.ILessonService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/lesson")
public class LessonController {

    private final ILessonService lessonService;

    public LessonController(ILessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping({"/{direction}"})
    public Set<LessonDto> getLessonByDirection(@PathVariable String direction,
                                                @RequestBody FilterDto filterDto) {
        return lessonService.getLessonByDirection();
    }

    @PostMapping("/new-lesson")
    @ResponseStatus(HttpStatus.CREATED)
    public void createLesson(@AuthenticationPrincipal JwtUserDetails userDetails,
                             @RequestBody @Validated(LessonDto.Create.class) LessonDto lessonDto) {
        lessonService.createLesson(userDetails, lessonDto);
    }

    @PostMapping("/{id}/review")
    public CommentDto createComment(@AuthenticationPrincipal JwtUserDetails userDetails,
                                    @RequestBody @Validated(CourseDto.Create.class) CommentDto commentDto) {
        return lessonService.createComment(userDetails,commentDto);
    }

    @PutMapping("/{id}/settings")
    public LessonDto editLesson(@PathVariable Long id, @RequestBody LessonDto lessonDto) {
        return lessonService.editLesson(id,lessonDto);
    }

    @PatchMapping("/{id}/subscribe")
    @ResponseStatus(HttpStatus.OK)
    public void subscribeToLesson(@PathVariable Long id, @AuthenticationPrincipal JwtUserDetails userDetails) {
        lessonService.subscribeToLesson(id,userDetails);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
    }
}
