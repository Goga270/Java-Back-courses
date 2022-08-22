package com.example.experienceexchange.service;

import com.example.experienceexchange.dto.CourseDto;
import com.example.experienceexchange.dto.LessonDto;
import com.example.experienceexchange.repository.interfaceRepo.ILessonRepository;
import com.example.experienceexchange.service.interfaceService.ILessonService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class LessonService implements ILessonService {

    private final ILessonRepository lessonRepository;

    public LessonService(ILessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    @Override
    public void createLesson(LessonDto lessonDto) {

    }

    @Override
    public CourseDto editLesson(Long id, LessonDto lessonDto) {
        return null;
    }

    @Override
    public void deleteLesson(Long id) {

    }

    @Override
    public Set<LessonDto> getLessonByDirection() {
        return null;
    }
}
