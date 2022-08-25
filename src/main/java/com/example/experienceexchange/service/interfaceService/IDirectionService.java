package com.example.experienceexchange.service.interfaceService;

import com.example.experienceexchange.dto.DirectionDto;

import java.util.List;

public interface IDirectionService {
    DirectionDto createDirection(DirectionDto directionDto);

    DirectionDto editDirection(Long id, DirectionDto directionDto);

    void deleteDirection(Long id);

    List<DirectionDto> getAllDirections();

    DirectionDto getDirection(Long id);
}
