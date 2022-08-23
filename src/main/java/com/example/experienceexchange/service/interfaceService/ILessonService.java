package com.example.experienceexchange.service.interfaceService;

import com.example.experienceexchange.dto.CommentDto;
import com.example.experienceexchange.dto.LessonDto;
import com.example.experienceexchange.security.JwtUserDetails;

import java.util.Set;

public interface ILessonService {

    void createLesson(JwtUserDetails userDetails, LessonDto lessonDto);

    LessonDto editLesson(Long id, LessonDto lessonDto);

    void deleteLesson(Long id);

    Set<LessonDto> getLessonByDirection();

    void subscribeToLesson(Long id, JwtUserDetails userDetails);

    CommentDto createComment(JwtUserDetails userDetails, CommentDto commentDto);
}
