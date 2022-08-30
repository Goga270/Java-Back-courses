package com.example.experienceexchange.util.mapper;

import com.example.experienceexchange.dto.LessonOnCourseDto;
import com.example.experienceexchange.model.LessonOnCourse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LessonOnCourseMapper {
    @Mapping(target = "courseId", expression = "java(lesson.getCourse().getId())")
    LessonOnCourseDto lessonOnCourseToLessonOnCourseDto(LessonOnCourse lesson);

    @Mapping(target = "course", ignore = true)
    LessonOnCourse lessonOnCourseDtoToLessonOnCourse(LessonOnCourseDto lesson);
}
