package com.example.experienceexchange.controller;

import com.example.experienceexchange.dto.CourseDto;
import com.example.experienceexchange.dto.FilterDto;
import com.example.experienceexchange.dto.LessonDto;
import com.example.experienceexchange.service.interfaceService.ILessonService;
import org.springframework.http.HttpStatus;
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

    @PostMapping("new-lesson")
    @ResponseStatus(HttpStatus.CREATED)
    public void createLesson(@RequestBody @Validated(LessonDto.Create.class) LessonDto lessonDto) {
        lessonService.createLesson(lessonDto);
    }

    @PutMapping("{id}/settings")
    public CourseDto editLesson(@PathVariable Long id, @RequestBody LessonDto lessonDto) {
        return lessonService.editLesson(id,lessonDto);
    }

    @DeleteMapping("{id}")
    public void deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
    }
}
