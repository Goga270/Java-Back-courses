package com.example.experienceexchange.controller;


import com.example.experienceexchange.dto.CourseDto;
import com.example.experienceexchange.service.interfaceService.ICourseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final ICourseService courseService;

    public CourseController(ICourseService courseService) {
        this.courseService = courseService;
    }

    // TODO : all - для всех направлений
    @GetMapping("'/{direction}/{section}")
    public Set<CourseDto> getCoursesByDirectionSection(@PathVariable String direction,
                                                       @PathVariable(required = false) String section) {
        if (section == null) {
            section = "all";
        }
        return courseService.getCoursesByDirection();
    }

    @GetMapping("{direction}/{section}")
    public Set<CourseDto> getCoursesInDirectionSection(@PathVariable String direction, @PathVariable String section) {
        return null;
    }

}
