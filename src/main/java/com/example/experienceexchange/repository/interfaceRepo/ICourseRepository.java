package com.example.experienceexchange.repository.interfaceRepo;

import com.example.experienceexchange.model.Course;

import java.util.List;

public interface ICourseRepository extends GenericDao<Course, Long> {

    List<Course> findAllCoursesByFilter(String filter);
}
