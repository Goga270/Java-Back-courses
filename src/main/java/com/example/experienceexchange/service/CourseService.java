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
}
