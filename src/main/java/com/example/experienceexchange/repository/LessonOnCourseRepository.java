package com.example.experienceexchange.repository;

import com.example.experienceexchange.model.LessonOnCourse;
import com.example.experienceexchange.repository.interfaceRepo.ILessonOnCourseRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class LessonOnCourseRepository extends HibernateAbstractDao<LessonOnCourse, Long> implements ILessonOnCourseRepository {

    private static final String JPQL_FIND_LESSONS_BY_USER =
            "SELECT DISTINCT ln FROM LessonOnCourse ln " +
                    "JOIN FETCH ln.course cr " +
                    "JOIN FETCH cr.usersInCourse us " +
                    "WHERE us.id = :userId " +
                    "ORDER BY ln.startLesson ASC";

    private static final String JPQL_FIND_LESSONS_BY_USER_BY_COURSE =
            "SELECT DISTINCT ln FROM LessonOnCourse ln " +
                    "JOIN FETCH ln.course cr " +
                    "JOIN FETCH cr.usersInCourse us " +
                    "WHERE cr.id= :courseId " +
                    "AND us.id = :userId " +
                    "ORDER BY ln.startLesson ASC";

    private static final String JPQL_FIND_LESSON_BY_ID_IN_COURSE =
            "SELECT DISTINCT ln FROM LessonOnCourse ln " +
                    "JOIN FETCH ln.course cr " +
                    "JOIN FETCH cr.usersInCourse us " +
                    "WHERE cr.id=:courseId " +
                    "AND us.id = :userId " +
                    "AND ln.id = :lessonId ";

    @Override
    public List<LessonOnCourse> findAllLessonsOnSubscribedCoursesByUserId(Long userId) {
        TypedQuery<LessonOnCourse> query = entityManager.createQuery(JPQL_FIND_LESSONS_BY_USER, getClassEntity());

        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public List<LessonOnCourse> findAllLessonsOnCourseByUserIdAndCourseId(Long userId, Long courseId) {
        TypedQuery<LessonOnCourse> query = entityManager.createQuery(JPQL_FIND_LESSONS_BY_USER_BY_COURSE, getClassEntity());

        query.setParameter("userId", userId);
        query.setParameter("courseId", courseId);
        return query.getResultList();
    }

    @Override
    public LessonOnCourse findLessonInCourseForSubscriber(Long userId, Long courseId, Long lessonId) {
        TypedQuery<LessonOnCourse> query = entityManager.createQuery(JPQL_FIND_LESSON_BY_ID_IN_COURSE, getClassEntity());

        query.setParameter("userId", userId);
        query.setParameter("courseId", courseId);
        query.setParameter("lessonId", lessonId);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
