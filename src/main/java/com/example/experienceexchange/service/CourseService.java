package com.example.experienceexchange.service;

import com.example.experienceexchange.dto.CommentDto;
import com.example.experienceexchange.dto.CourseDto;
import com.example.experienceexchange.security.JwtUserDetails;
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
    public void createCourse(JwtUserDetails userDetails, CourseDto courseDto) {

    }

    @Override
    public CourseDto editCourse(Long id, CourseDto courseDto) {
        return null;
    }

    @Override
    public void deleteCourse(Long id) {

    }

    @Override
    public void subscribeToCourse(Long id, JwtUserDetails userDetails) {

    }

    @Override
    public CommentDto createComment(JwtUserDetails userDetails, CommentDto commentDto) {
        return null;
    }
}
