package com.example.experienceexchange.service.interfaceService;

import com.example.experienceexchange.dto.CommentDto;
import com.example.experienceexchange.dto.LessonDto;
import com.example.experienceexchange.dto.LessonOnCourseDto;
import com.example.experienceexchange.dto.PaymentDto;
import com.example.experienceexchange.security.JwtUserDetails;

import java.util.List;

public interface ILessonService {

    LessonDto createLesson(JwtUserDetails userDetails, LessonDto lessonDto);

    LessonDto editLesson(JwtUserDetails userDetails, Long id, LessonDto lessonDto);

    void deleteLesson(JwtUserDetails userDetails, Long id);

    List<LessonDto> getLessonByDirection();

    PaymentDto subscribeToLesson(JwtUserDetails userDetails, PaymentDto paymentDto, Long lessonId);

    LessonDto getLesson(Long lessonId);

    List<LessonDto> getSchedule(JwtUserDetails userDetails);
}
