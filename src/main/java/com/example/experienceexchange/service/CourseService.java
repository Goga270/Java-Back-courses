package com.example.experienceexchange.service;

import com.example.experienceexchange.dto.CourseDto;
import com.example.experienceexchange.service.interfaceService.ICourseService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CourseService implements ICourseService {
    @Override
    public Set<CourseDto> getCoursesByDirection() {
        return null;
    }

    @Override
    public void createCourse(CourseDto courseDto) {

    }

    @Override
    public CourseDto editCourse(Long id, CourseDto courseDto) {
        return null;
    }

    @Override
    public void deleteCourse(Long id) {

    }
}
