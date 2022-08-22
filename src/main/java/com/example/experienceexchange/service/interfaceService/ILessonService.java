package com.example.experienceexchange.service.interfaceService;

import com.example.experienceexchange.dto.CourseDto;
import com.example.experienceexchange.dto.LessonDto;

import java.util.Set;

public interface ILessonService {

    void createLesson(LessonDto lessonDto);

    CourseDto editLesson(Long id, LessonDto lessonDto);

    void deleteLesson(Long id);

    Set<LessonDto> getLessonByDirection();

}
