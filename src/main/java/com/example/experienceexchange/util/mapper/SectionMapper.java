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

    public Section sectionDtoToSection(SectionDto sectionDto) {
        if (sectionDto == null) {
            return null;
        }
        Section section = new Section();
        section.setId(sectionDto.getId());
        section.setName(sectionDto.getName());
        Direction directionForSection = directionRepository.find(sectionDto.getDirectionId());
        if (directionForSection == null) {
            throw new DirectionNotFoundException(sectionDto.getDirectionId());
        }
        section.setDirection(directionForSection);
        return section;
    }
}
