package com.example.experienceexchange.service.interfaceService;

import com.example.experienceexchange.dto.CommentDto;
import com.example.experienceexchange.dto.CourseDto;
import com.example.experienceexchange.dto.LessonOnCourseDto;
import com.example.experienceexchange.security.JwtUserDetails;

import java.util.List;

public interface ICourseService {

    List<CourseDto> getCoursesByDirection();

    CourseDto createCourse(JwtUserDetails userDetails, CourseDto courseDto);

    CourseDto editCourse(Long id, CourseDto courseDto);

    void deleteCourse(Long id);

    void subscribeToCourse(Long id, JwtUserDetails userDetails);

    CommentDto createComment(Long courseId, JwtUserDetails userDetails, CommentDto commentDto);

    List<CommentDto> getCommentsByCourse(Long courseId);

    CourseDto createLesson(JwtUserDetails userDetails, Long courseId, LessonOnCourseDto lesson);
}
