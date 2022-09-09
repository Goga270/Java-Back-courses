package com.example.experienceexchange.repository.interfaceRepo;

import com.example.experienceexchange.model.Course;

import java.util.List;
import java.util.Map;

public interface ICourseRepository extends GenericDao<Course, Long> {
    // TODO : ОБЯЗАТЕЛЬНО ПРОВЕРИТЬ ЧТОБЫ КУРС ЕЩЕ НЕ НАЧАЛСЯ
    // "section.id" "direction.id" "skill.id" "sc.skilllevel"
    // TODO : ЗАЛОГИРОВАТЬ ЧТОБЫ ЗНАТЬ КАКОЙ СКРИПТ УЛЕТЕЛ В БАЗУ ДАННЫХ
    List<Course> findAllCourseByFilter(String filter);
}
