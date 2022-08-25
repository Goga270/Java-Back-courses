package com.example.experienceexchange.util.mapper;

import com.example.experienceexchange.dto.DirectionDto;
import com.example.experienceexchange.dto.SectionDto;
import com.example.experienceexchange.exception.DirectionNotFoundException;
import com.example.experienceexchange.model.Direction;
import com.example.experienceexchange.model.Section;
import com.example.experienceexchange.repository.interfaceRepo.IDirectionRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public abstract class DirectionMapper {

    public abstract DirectionDto directionToDirectionDto(Direction direction);

    public abstract Direction directionDtoToDirection(DirectionDto directionDto);

    public abstract List<DirectionDto> toDirectionDto(Collection<Direction> directions);

    @Mapping(target = "directionId", expression = "java(section.getDirection().getId())")
    public abstract SectionDto sectionToSectionDto(Section section);

    @Mapping(target = "direction", ignore = true)
    public abstract Section sectionDtoToSection(SectionDto sectionDto);
}
