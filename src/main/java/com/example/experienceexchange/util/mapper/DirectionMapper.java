package com.example.experienceexchange.util.mapper;

import com.example.experienceexchange.dto.DirectionDto;
import com.example.experienceexchange.dto.SectionDto;
import com.example.experienceexchange.model.Direction;
import com.example.experienceexchange.model.Section;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface DirectionMapper {

    DirectionDto directionToDirectionDto(Direction direction);

    Direction directionDtoToDirection(DirectionDto directionDto);

    List<DirectionDto> toDirectionDto(Collection<Direction> directions);

    @Mapping(target = "directionId", expression = "java(section.getDirection().getId())")
    SectionDto sectionToSectionDto(Section section);

    @Mapping(target = "direction", ignore = true)
    Section sectionDtoToSection(SectionDto sectionDto);
}
