package com.example.experienceexchange.util.mapper;

import com.example.experienceexchange.dto.CourseDto;
import com.example.experienceexchange.dto.DirectionDto;
import com.example.experienceexchange.dto.LessonOnCourseDto;
import com.example.experienceexchange.dto.SectionDto;
import com.example.experienceexchange.model.Course;
import com.example.experienceexchange.model.Direction;
import com.example.experienceexchange.model.LessonOnCourse;
import com.example.experienceexchange.model.Section;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class CourseMapper {

    @Mapping(target = "author", ignore = true)
    @Mapping(target = "currentNumberUsers", defaultValue = "0")
    public abstract Course courseDtoToCourse(CourseDto courseDto);

    @Mapping(target = "authorId", expression = "java(course.getAuthor().getId())")
    @Mapping(target = "currentNumberUsers", defaultValue = "0")
    public abstract CourseDto courseToCourseDto(Course course);

    @Mapping(target = "directionId", expression = "java(section.getDirection().getId())")
    public abstract SectionDto sectionToSectionDto(Section section);

    public abstract List<CourseDto> toCourseDto(Collection<Course> courses);

    public abstract DirectionDto directionToDirectionDto(Direction direction);
    @Mapping(target = "courseId", expression = "java(lessonOnCourse.getCourse().getId())")
    public abstract LessonOnCourseDto lessonOnCourseToLessonOnCourseDto(LessonOnCourse lessonOnCourse);

    @Mapping(target = "course", ignore = true)
    public abstract LessonOnCourse lessonOnCourseDtoToLessonOnCourse(LessonOnCourseDto lessonOnCourseDto);
}
