package com.example.experienceexchange.util.mapper;

import com.example.experienceexchange.dto.SectionDto;
import com.example.experienceexchange.exception.DirectionNotFoundException;
import com.example.experienceexchange.model.Direction;
import com.example.experienceexchange.model.Section;
import com.example.experienceexchange.repository.interfaceRepo.IDirectionRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class SectionMapper {

    @Autowired
    protected IDirectionRepository directionRepository;

    @Mapping(target = "directionId", expression = "java(section.getDirection().getId())")
    public abstract SectionDto sectionToSectionDto(Section section);
    @Mapping(target = "direction", ignore = true)
    public abstract Section sectionDtoToSection(SectionDto section);

    public Section updateSection(Section oldSection, SectionDto sectionDto) {
        oldSection.setName(sectionDto.getName());
        Direction directionForSection = directionRepository.find(sectionDto.getDirectionId());
        if (directionForSection == null) {
            throw new DirectionNotFoundException(sectionDto.getDirectionId());
        }
        oldSection.setDirection(directionForSection);
        return oldSection;
    }
}
