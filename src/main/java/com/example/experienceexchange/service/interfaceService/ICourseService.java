package com.example.experienceexchange.service.interfaceService;

import com.example.experienceexchange.dto.CourseDto;
import com.example.experienceexchange.dto.LessonOnCourseDto;
import com.example.experienceexchange.dto.PaymentDto;
import com.example.experienceexchange.repository.filter.SearchCriteria;
import com.example.experienceexchange.security.JwtUserDetails;

import java.util.List;

public interface ICourseService {

    List<CourseDto> getCourses(List<SearchCriteria> filterDto);

    CourseDto createCourse(JwtUserDetails userDetails, CourseDto courseDto);

    CourseDto editCourse(JwtUserDetails userDetails, Long id, CourseDto courseDto);

    CourseDto getCourse(Long courseId);

    void deleteCourse(JwtUserDetails userDetails, Long id);

    PaymentDto subscribeToCourse(JwtUserDetails userDetails, PaymentDto paymentDto, Long courseId);

    CourseDto createLessonOnCourse(JwtUserDetails userDetails, Long courseId, LessonOnCourseDto lesson);

    List<LessonOnCourseDto> getSchedule(JwtUserDetails userDetails);

    List<LessonOnCourseDto> getScheduleByCourse(JwtUserDetails userDetails, Long courseId);

    LessonOnCourseDto getLessonOnCourse(JwtUserDetails userDetails, Long courseId, Long lessonId);
}
