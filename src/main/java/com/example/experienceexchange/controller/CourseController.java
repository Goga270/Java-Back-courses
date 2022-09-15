package com.example.experienceexchange.controller;


import com.example.experienceexchange.dto.CourseDto;
import com.example.experienceexchange.dto.LessonOnCourseDto;
import com.example.experienceexchange.dto.PaymentDto;
import com.example.experienceexchange.repository.filter.SearchCriteria;
import com.example.experienceexchange.security.JwtUserDetails;
import com.example.experienceexchange.service.interfaceService.ICourseService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/courses")
public class CourseController {

    private final ICourseService courseService;

    public CourseController(ICourseService courseService) {
        this.courseService = courseService;
    }

    @JsonView({CourseDto.Details.class})
    @GetMapping("")
    public List<CourseDto> getCoursesByFilter(@RequestBody @Valid List<SearchCriteria> filters) {
        return courseService.getCourses(filters);
    }

    @JsonView({CourseDto.Details.class})
    @GetMapping("/{id}")
    public CourseDto getCourse(@PathVariable("id") Long courseId) {
        return courseService.getCourse(courseId);
    }

    @JsonView({LessonOnCourseDto.DetailsForSubscribe.class})
    @GetMapping("/{courseId}/{lessonId}")
    public LessonOnCourseDto getLessonOnCourse(
            @AuthenticationPrincipal JwtUserDetails userDetails,
            @PathVariable Long courseId,
            @PathVariable Long lessonId) {
        return courseService.getLessonOnCourse(userDetails, courseId, lessonId);
    }

    @JsonView(LessonOnCourseDto.DetailsForTimetable.class)
    @GetMapping("/my-schedule-courses")
    public List<LessonOnCourseDto> getSchedule(@AuthenticationPrincipal JwtUserDetails userDetails) {
        return courseService.getSchedule(userDetails);
    }

    @JsonView(LessonOnCourseDto.DetailsForTimetable.class)
    @GetMapping("/{id}/my-schedule-course")
    public List<LessonOnCourseDto> getScheduleByCourse(
            @AuthenticationPrincipal JwtUserDetails userDetails,
            @PathVariable("id") Long courseId) {
        return courseService.getScheduleByCourse(userDetails, courseId);
    }

    @PostMapping("/new-course")
    @ResponseStatus(HttpStatus.CREATED)
    public CourseDto createCourse(
            @AuthenticationPrincipal JwtUserDetails userDetails,
            @RequestBody @Validated(CourseDto.Create.class) CourseDto courseDto) {
        return courseService.createCourse(userDetails, courseDto);
    }

    @PostMapping("/{id}/settings/new-lesson")
    @ResponseStatus(HttpStatus.CREATED)
    public CourseDto createLesson(
            @AuthenticationPrincipal JwtUserDetails userDetails,
            @PathVariable("id") Long courseId,
            @RequestBody @Validated({LessonOnCourseDto.Create.class}) LessonOnCourseDto lesson) {
        return courseService.createLessonOnCourse(userDetails, courseId, lesson);
    }

    @PutMapping("/{id}/settings")
    public CourseDto editCourse(
            @AuthenticationPrincipal JwtUserDetails userDetails,
            @PathVariable Long id,
            @RequestBody @Validated(value = CourseDto.Edit.class) CourseDto courseDto) {
        return courseService.editCourse(userDetails, id, courseDto);
    }

    @PutMapping("/restart-course")
    public CourseDto restartCourse(
            @AuthenticationPrincipal JwtUserDetails userDetails,
            @RequestBody @Validated(value = CourseDto.Edit.class) CourseDto courseDto) {
        return courseService.restartCourse(userDetails, courseDto);
    }

    @JsonView({PaymentDto.CreateCourse.class})
    @PostMapping("/{id}/subscribe")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentDto subscribeToCourse(
            @AuthenticationPrincipal JwtUserDetails userDetails,
            @PathVariable("id") Long courseId,
            @RequestBody @Validated(PaymentDto.CreateCourse.class) PaymentDto paymentDto) {
        return courseService.subscribeToCourse(userDetails, paymentDto, courseId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCourse(
            @AuthenticationPrincipal JwtUserDetails userDetails,
            @PathVariable("id") Long courseId) {
        courseService.deleteCourse(userDetails, courseId);
    }
}
