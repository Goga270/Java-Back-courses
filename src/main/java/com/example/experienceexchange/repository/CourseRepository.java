package com.example.experienceexchange.repository;

import com.example.experienceexchange.model.Course;
import com.example.experienceexchange.repository.interfaceRepo.ICourseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Slf4j
@Repository
public class CourseRepository extends HibernateAbstractDao<Course, Long> implements ICourseRepository {

    private static final String JPQL_FILTER_COURSE =
            "SELECT DISTINCT course FROM Course course " +
                    "JOIN  course.sections section " +
                    "JOIN  course.directions  direction " +
                    "JOIN  course.skills skill " +
                    "WHERE course.dateStart > now() " +
                    "%s";
    // TODO : ЗАЛОГИРОВАТЬ ЧТОБЫ ЗНАТЬ КАКОЙ СКРИПТ УЛЕТЕЛ В БАЗУ ДАННЫХ
    @Override
    public List<Course> findAllCourseByFilter(String filter) {
        String jpqlQuery = String.format(JPQL_FILTER_COURSE, filter);
        TypedQuery<Course> query = entityManager.createQuery(jpqlQuery, getClassEntity());
        return query.getResultList();
    }
}
