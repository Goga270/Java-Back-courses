package com.example.experienceexchange.repository;

import com.example.experienceexchange.model.Lesson;
import com.example.experienceexchange.model.LessonSingle;
import com.example.experienceexchange.repository.interfaceRepo.ILessonRepository;
import org.springframework.stereotype.Repository;

@Repository
public class LessonRepository extends HibernateAbstractDao<LessonSingle,Long> implements ILessonRepository {
}
