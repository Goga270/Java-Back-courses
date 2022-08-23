package com.example.experienceexchange.service.interfaceService;

import com.example.experienceexchange.dto.CommentDto;
import com.example.experienceexchange.dto.CourseDto;
import com.example.experienceexchange.security.JwtUserDetails;

import java.util.Set;

public interface ICourseService {
    Set<CourseDto> getCoursesByDirection();

    void createCourse(JwtUserDetails userDetails, CourseDto courseDto);

    CourseDto editCourse(Long id, CourseDto courseDto);

    void deleteCourse(Long id);

    void subscribeToCourse(Long id, JwtUserDetails userDetails);

    CommentDto createComment(JwtUserDetails userDetails, CommentDto commentDto);
}
