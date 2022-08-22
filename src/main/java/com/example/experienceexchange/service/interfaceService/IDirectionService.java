package com.example.experienceexchange.service.interfaceService;

public interface IDirectionService {
    void createDirection(DirectionDto directionDto);

    DirectionDto editDirection(Long id, DirectionDto directionDto);

    void deleteDirection(Long id);

}
