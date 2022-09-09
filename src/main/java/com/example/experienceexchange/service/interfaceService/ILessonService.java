package com.example.experienceexchange.service.interfaceService;

import com.example.experienceexchange.dto.LessonDto;
import com.example.experienceexchange.dto.PaymentDto;
import com.example.experienceexchange.repository.filter.SearchCriteria;
import com.example.experienceexchange.security.JwtUserDetails;

import java.util.List;

public interface ILessonService {

    List<LessonDto> getLessons(List<SearchCriteria> filters);

    LessonDto getLesson(Long lessonId);

    LessonDto getLesson(JwtUserDetails userDetails, Long lessonId);

    List<LessonDto> getSchedule(JwtUserDetails userDetails);

    LessonDto createLesson(JwtUserDetails userDetails, LessonDto lessonDto);

    LessonDto editLesson(JwtUserDetails userDetails, Long lessonId, LessonDto lessonDto);

    PaymentDto subscribeToLesson(JwtUserDetails userDetails, PaymentDto paymentDto, Long lessonId);

    void deleteLesson(JwtUserDetails userDetails, Long id);
}
