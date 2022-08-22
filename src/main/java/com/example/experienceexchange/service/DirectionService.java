package com.example.experienceexchange.service;

import com.example.experienceexchange.repository.interfaceRepo.IDirectionRepository;
import com.example.experienceexchange.service.interfaceService.DirectionDto;
import com.example.experienceexchange.service.interfaceService.IDirectionService;
import org.springframework.stereotype.Service;

@Service
public class DirectionService implements IDirectionService {

    private final IDirectionRepository directionRepository;

    public DirectionService(IDirectionRepository directionRepository) {
        this.directionRepository = directionRepository;
    }

    @Override
    public void createDirection(DirectionDto directionDto) {

    }

    @Override
    public DirectionDto editDirection(Long id, DirectionDto directionDto) {
        return null;
    }

    @Override
    public void deleteDirection(Long id) {

    }
}
