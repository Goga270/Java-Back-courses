package com.example.experienceexchange.service.interfaceService;

import com.example.experienceexchange.dto.CommentDto;
import com.example.experienceexchange.dto.CourseDto;
import com.example.experienceexchange.dto.LessonOnCourseDto;
import com.example.experienceexchange.dto.PaymentDto;
import com.example.experienceexchange.security.JwtUserDetails;

import java.util.List;

public interface ICourseService {

    List<CourseDto> getCoursesByDirection();

    CourseDto createCourse(JwtUserDetails userDetails, CourseDto courseDto);

    CourseDto editCourse(JwtUserDetails userDetails, Long id, CourseDto courseDto);

    CourseDto getCourse(Long courseId);

    void deleteCourse(JwtUserDetails userDetails, Long id);

    PaymentDto subscribeToCourse(JwtUserDetails userDetails, PaymentDto paymentDto, Long courseId);

    CommentDto createComment(Long courseId, JwtUserDetails userDetails, CommentDto commentDto);

    List<CommentDto> getCommentsByCourse(Long courseId);

    CourseDto createLesson(JwtUserDetails userDetails, Long courseId, LessonOnCourseDto lesson);
}
