package com.example.experienceexchange.repository.interfaceRepo;

import com.example.experienceexchange.dto.LessonOnCourseDto;
import com.example.experienceexchange.model.LessonOnCourse;

import java.util.List;

public interface ILessonOnCourseRepository extends GenericDao<LessonOnCourse,Long> {
    List<LessonOnCourse> findAllLessonsOnCourseByUserId(Long userId);

    List<LessonOnCourse> findAllLessonsOnCourseByUserIdAndCourseId(Long userId, Long courseId);
}
