package com.example.experienceexchange.controller;

import com.example.experienceexchange.dto.CourseDto;
import com.example.experienceexchange.dto.LessonDto;
import com.example.experienceexchange.dto.PaymentDto;
import com.example.experienceexchange.repository.filter.SearchCriteria;
import com.example.experienceexchange.security.JwtUserDetails;
import com.example.experienceexchange.service.interfaceService.ILessonService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/lessons")
public class LessonController {

    private final ILessonService lessonService;

    public LessonController(ILessonService lessonService) {
        this.lessonService = lessonService;
    }

    @JsonView({LessonDto.Details.class})
    @GetMapping({""})
    public List<LessonDto> getLessons(@RequestBody @Valid List<SearchCriteria> filters) {
        return lessonService.getLessons(filters);
    }

    @JsonView({LessonDto.Details.class})
    @GetMapping("/{id}")
    public LessonDto getLesson(@PathVariable("id") Long lessonId) {
        return lessonService.getLesson(lessonId);
    }

    @JsonView({LessonDto.DetailsForSubscriber.class})
    @GetMapping("/subscriptions/{id}")
    public LessonDto getLesson(@AuthenticationPrincipal JwtUserDetails userDetails,
                               @PathVariable("id") Long lessonId) {
        return lessonService.getLesson(userDetails, lessonId);
    }

    @JsonView({LessonDto.DetailsForTimetable.class})
    @GetMapping("/schedule-my-lessons")
    public List<LessonDto> getScheduleBySingleLesson(@AuthenticationPrincipal JwtUserDetails userDetails) {
        return lessonService.getSchedule(userDetails);
    }

    @PostMapping("/new-lesson")
    @ResponseStatus(HttpStatus.CREATED)
    public LessonDto createLesson(@AuthenticationPrincipal JwtUserDetails userDetails,
                                  @RequestBody @Validated(LessonDto.Create.class) LessonDto lessonDto) {
        return lessonService.createLesson(userDetails, lessonDto);
    }

    @JsonView({PaymentDto.CreateLesson.class})
    @PostMapping("/{id}/subscribe")
    @ResponseStatus(HttpStatus.OK)
    public PaymentDto subscribeToLesson(@AuthenticationPrincipal JwtUserDetails userDetails,
                                        @PathVariable("id") Long lessonId,
                                        @RequestBody @Validated(PaymentDto.CreateLesson.class) PaymentDto paymentDto) {
        return lessonService.subscribeToLesson(userDetails, paymentDto, lessonId);
    }

    @PutMapping("/restart-lesson")
    public LessonDto restartCourse(@AuthenticationPrincipal JwtUserDetails userDetails,
                                   @RequestBody @Validated(value = LessonDto.Edit.class) LessonDto lessonDto) {
        return lessonService.restartLesson(userDetails, lessonDto);
    }

    @PutMapping("/{lessonId}/settings")
    public LessonDto editLesson(@AuthenticationPrincipal JwtUserDetails userDetails,
                                @PathVariable Long lessonId,
                                @RequestBody @Validated(LessonDto.Edit.class) LessonDto lessonDto) {
        return lessonService.editLesson(userDetails, lessonId, lessonDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLesson(@AuthenticationPrincipal JwtUserDetails userDetails,
                             @PathVariable("id") Long lessonId) {
        lessonService.deleteLesson(userDetails, lessonId);
    }
}
