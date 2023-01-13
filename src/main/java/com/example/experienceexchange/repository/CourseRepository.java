package com.example.experienceexchange.repository;

import com.example.experienceexchange.model.Course;
import com.example.experienceexchange.model.Skill;
import com.example.experienceexchange.repository.interfaceRepo.ICourseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Slf4j
@Repository
public class CourseRepository extends HibernateAbstractDao<Course, Long> implements ICourseRepository {

    public static final String JPQL_FILTER_COURSE =
            "SELECT DISTINCT course FROM Course course " +
                    "JOIN FETCH course.sections section " +
                    "JOIN FETCH course.directions  direction " +
                    "JOIN FETCH course.skills skill ";

    @Override
    public List<Course> findAllCoursesByFilter(String filter) {
        String jpqlQuery = JPQL_FILTER_COURSE;
        TypedQuery<Course> query = entityManager.createQuery(jpqlQuery, getClassEntity());
        log.debug("Find courses with filter {}", filter);
        return query.getResultList();
    }

    @Override
    public List<Skill> getSkills() {
        return entityManager
                .createQuery("SELECT  skill FROM Skill skill", Skill.class)
                .getResultList();
    }
}
