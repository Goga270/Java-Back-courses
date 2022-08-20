package com.example.experienceexchange.service.interfaceService;

import com.example.experienceexchange.dto.CourseDto;

import java.util.Set;

public interface ICourseService {
    Set<CourseDto> getCoursesByDirection();
}
