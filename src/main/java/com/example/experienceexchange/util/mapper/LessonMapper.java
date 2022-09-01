package com.example.experienceexchange.util.mapper;

import com.example.experienceexchange.dto.DirectionDto;
import com.example.experienceexchange.dto.LessonDto;
import com.example.experienceexchange.dto.SectionDto;
import com.example.experienceexchange.model.Direction;
import com.example.experienceexchange.model.LessonSingle;
import com.example.experienceexchange.model.Section;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    @Mapping(target = "author", ignore = true)
    @Mapping(target = "currentNumberUsers", ignore = true)
    LessonSingle lessonDtoToLesson(LessonDto lessonDto);

    @Mapping(target = "authorId", expression = "java(lessonSingle.getAuthor().getId())")
    @Mapping(target = "currentNumberUsers", defaultValue = "0")
    LessonDto lessonToLessonDto(LessonSingle lessonSingle);

    @Mapping(target = "directionId", expression = "java(section.getDirection().getId())")
    SectionDto sectionToSectionDto(Section section);

    List<LessonDto> toLessonDto(Collection<LessonSingle> courses);

    DirectionDto directionToDirectionDto(Direction direction);
}
