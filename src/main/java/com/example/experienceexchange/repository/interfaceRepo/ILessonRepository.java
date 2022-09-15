package com.example.experienceexchange.repository.interfaceRepo;

import com.example.experienceexchange.model.LessonSingle;

import java.util.List;

public interface ILessonRepository extends GenericDao<LessonSingle, Long> {

    List<LessonSingle> findAllLessonsByUserId(Long userId);

    List<LessonSingle> findAllLessonsByFilter(String filter);

    LessonSingle findLessonSingleByUserIdAndLessonId(Long userId, Long lessonId);
}
