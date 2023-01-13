package com.example.experienceexchange.util.mapper;

import com.example.experienceexchange.dto.DirectionDto;
import com.example.experienceexchange.dto.SkillDto;
import com.example.experienceexchange.model.Direction;
import com.example.experienceexchange.model.Skill;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface SkillMapper {

    List<SkillDto> toSkillDto(Collection<Skill> skills);
}
