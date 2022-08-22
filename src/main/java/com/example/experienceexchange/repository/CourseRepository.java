package com.example.experienceexchange.repository;

import com.example.experienceexchange.model.Course;
import com.example.experienceexchange.repository.HibernateAbstractDao;
import com.example.experienceexchange.repository.interfaceRepo.ICourseRepository;
import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.stereotype.Repository;

@Repository
public class CourseRepository extends HibernateAbstractDao<Course, Long> implements ICourseRepository {


}
