package com.example.experienceexchange.repository.interfaceRepo;

import com.example.experienceexchange.model.Course;
import com.example.experienceexchange.model.Skill;

import java.util.List;

public interface ICourseRepository extends GenericDao<Course, Long> {

    List<Course> findAllCoursesByFilter(String filter);

    List<Skill> getSkills();
}
