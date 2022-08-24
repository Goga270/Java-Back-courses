package com.example.experienceexchange.service.interfaceService;

import com.example.experienceexchange.dto.DirectionDto;
import com.example.experienceexchange.model.Direction;

import java.util.List;
import java.util.Set;

public interface IDirectionService {
    DirectionDto createDirection(DirectionDto directionDto);

    DirectionDto editDirection(Long id, DirectionDto directionDto);

    void deleteDirection(Long id);

    List<DirectionDto> getAllDirections();

    DirectionDto getDirection(Integer id);
}
