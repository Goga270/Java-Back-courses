package com.example.experienceexchange.repository.interfaceRepo;

import com.example.experienceexchange.model.LessonOnCourse;

import java.util.List;

public interface ILessonOnCourseRepository extends GenericDao<LessonOnCourse, Long> {

    List<LessonOnCourse> findAllLessonsOnSubscribedCoursesByUserId(Long userId);

    List<LessonOnCourse> findAllLessonsOnCourseByUserIdAndCourseId(Long userId, Long courseId);

    LessonOnCourse findLessonInCourseForSubscriber(Long userId, Long courseId, Long lessonId);
}
