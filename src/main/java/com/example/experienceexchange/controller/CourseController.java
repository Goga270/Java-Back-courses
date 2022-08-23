package com.example.experienceexchange.controller;


import com.example.experienceexchange.dto.CommentDto;
import com.example.experienceexchange.dto.CourseDto;
import com.example.experienceexchange.dto.FilterDto;
import com.example.experienceexchange.security.JwtUserDetails;
import com.example.experienceexchange.service.interfaceService.ICourseService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final ICourseService courseService;

    public CourseController(ICourseService courseService) {
        this.courseService = courseService;
    }


    @GetMapping({"/{direction}"})
    public Set<CourseDto> getCoursesByDirection(@PathVariable String direction,
                                                @RequestBody FilterDto filterDto) {
        return courseService.getCoursesByDirection();
    }

    @GetMapping("")
    public Set<CourseDto> getCourses(@RequestBody FilterDto filterDto) {
        return null;
    }

    @PostMapping("new-course")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCourse(@AuthenticationPrincipal JwtUserDetails userDetails,
                             @RequestBody @Validated(CourseDto.Create.class) CourseDto courseDto) {
        courseService.createCourse(userDetails, courseDto);
    }

    @PostMapping("{id}/review")
    public CommentDto createComment(@AuthenticationPrincipal JwtUserDetails userDetails,
                                   @RequestBody @Validated(CourseDto.Create.class) CommentDto commentDto) {
        return courseService.createComment(userDetails,commentDto);
    }

    @PutMapping("{id}/settings")
    public CourseDto editCourse(@PathVariable Long id, @RequestBody CourseDto courseDto) {
        return courseService.editCourse(id, courseDto);
    }

    @PatchMapping("{id}/subscribe")
    public void subscribeToCourse(@PathVariable Long id, @AuthenticationPrincipal JwtUserDetails userDetails) {
        courseService.subscribeToCourse(id,userDetails);
    }

    @DeleteMapping("{id}")
    public void deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
    }
}
