package com.example.experienceexchange.repository;

import com.example.experienceexchange.dto.LessonOnCourseDto;
import com.example.experienceexchange.model.LessonOnCourse;
import com.example.experienceexchange.repository.interfaceRepo.ILessonOnCourseRepository;
import org.springframework.stereotype.Repository;

@Repository
public class LessonOnCourseRepository extends HibernateAbstractDao<LessonOnCourse, Long> implements ILessonOnCourseRepository {
}
