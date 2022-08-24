package com.example.experienceexchange.util.mapper;

import com.example.experienceexchange.dto.DirectionDto;
import com.example.experienceexchange.model.Direction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class DirectionMapper {

    public abstract DirectionDto directionToDirectionDto(Direction direction);

    public abstract Direction directionDtoToDirection(DirectionDto directionDto);
}
