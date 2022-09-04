package com.example.experienceexchange.controller;


import com.example.experienceexchange.dto.CourseDto;
import com.example.experienceexchange.dto.FilterDto;
import com.example.experienceexchange.dto.LessonOnCourseDto;
import com.example.experienceexchange.dto.PaymentDto;
import com.example.experienceexchange.security.JwtUserDetails;
import com.example.experienceexchange.service.interfaceService.ICourseService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO: можно удалить только свой курс, или можно удалить только свой комментарий, но мб админ может что он хочет
@RestController
@RequestMapping("/courses")
public class CourseController {

    private final ICourseService courseService;

    public CourseController(ICourseService courseService) {
        this.courseService = courseService;
    }


    /*@GetMapping({"/{direction}"})
    public Set<CourseDto> getCoursesByDirection(@PathVariable String direction,
                                                @RequestBody FilterDto filterDto) {
        return null;
        //return courseService.getCoursesByDirection();
    }*/

    @JsonView({CourseDto.Details.class})
    @GetMapping("")
    public List<CourseDto> getCourses(@RequestBody FilterDto filterDto) {
        return courseService.getCoursesByDirection();
    }

    @JsonView({CourseDto.Details.class})
    @GetMapping("/{id}")
    public CourseDto getCourse(@PathVariable("id") Long courseId) {
        return courseService.getCourse(courseId);
    }

    @PostMapping("/new-course")
    @ResponseStatus(HttpStatus.CREATED)
    public CourseDto createCourse(@AuthenticationPrincipal JwtUserDetails userDetails,
                                  @RequestBody @Validated(CourseDto.Create.class) CourseDto courseDto) {
        return courseService.createCourse(userDetails, courseDto);
    }

    @PostMapping("/{id}/settings/new-lesson")
    public CourseDto createLesson(
            @AuthenticationPrincipal JwtUserDetails userDetails,
            @PathVariable("id") Long courseId,
            @RequestBody @Validated({LessonOnCourseDto.Create.class}) LessonOnCourseDto lesson) {
        return courseService.createLesson(userDetails, courseId, lesson);
    }

    @PutMapping("/{id}/settings")
    public CourseDto editCourse(@AuthenticationPrincipal JwtUserDetails userDetails,
                                @PathVariable Long id,
                                @RequestBody @Validated(value = CourseDto.Edit.class) CourseDto courseDto) {
        return courseService.editCourse(userDetails, id, courseDto);
    }

    // TODO : А ТВОЙ ЛИ ЭТО КУРС ПРОВЕРИТЬ НАДО
    @JsonView({PaymentDto.DetailsForPayCourse.class})
    @PostMapping("/{id}/subscribe")
    @ResponseStatus(HttpStatus.OK)
    public PaymentDto subscribeToCourse(@AuthenticationPrincipal JwtUserDetails userDetails,
                                        @PathVariable("id") Long courseId,
                                        @RequestBody @Validated(PaymentDto.Create.class) PaymentDto paymentDto) {
        return courseService.subscribeToCourse(userDetails, paymentDto, courseId);
    }

    // TODO : А ТВОЙ ЛИ ЭТО КУРС ПРОВЕРИТЬ НАДО
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCourse(@AuthenticationPrincipal JwtUserDetails userDetails,
                             @PathVariable Long id) {
        courseService.deleteCourse(userDetails, id);
    }
}
