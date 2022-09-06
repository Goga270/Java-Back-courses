package com.example.experienceexchange.repository;

import com.example.experienceexchange.model.Lesson;
import com.example.experienceexchange.model.LessonOnCourse;
import com.example.experienceexchange.model.LessonSingle;
import com.example.experienceexchange.repository.interfaceRepo.ILessonRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class LessonRepository extends HibernateAbstractDao<LessonSingle,Long> implements ILessonRepository {
    private static final String JPQL_FIND_LESSONS_BY_USER = "SELECT ln FROM LessonSingle ln JOIN FETCH ln.usersInLesson u WHERE u.id =: userId ORDER BY ln.startLesson ASC";


    @Override
    public List<LessonSingle> findAllLessonsByUserId(Long userId) {
        TypedQuery<LessonSingle> query = entityManager.createQuery(JPQL_FIND_LESSONS_BY_USER, getClassEntity());
        query.setParameter("userId", userId);
        return query.getResultList();
    }
}
