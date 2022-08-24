package com.example.experienceexchange.service;

import com.example.experienceexchange.exception.DirectionNotFoundException;
import com.example.experienceexchange.model.Direction;
import com.example.experienceexchange.repository.interfaceRepo.IDirectionRepository;
import com.example.experienceexchange.dto.DirectionDto;
import com.example.experienceexchange.service.interfaceService.IDirectionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DirectionService implements IDirectionService {

    private final IDirectionRepository directionRepository;

    public DirectionService(IDirectionRepository directionRepository) {
        this.directionRepository = directionRepository;
    }

    @Transactional
    @Override
    public List<DirectionDto> getAllDirections() {
        List<Direction> directions = directionRepository.findAll();

        return null;
    }

    @Override
    public DirectionDto getDirection(Integer id) {
        return null;
    }


    @Override
    public DirectionDto createDirection(DirectionDto directionDto) {
        return null;
    }

    @Override
    public DirectionDto editDirection(Long id, DirectionDto directionDto) {
        return null;
    }

    @Override
    public void deleteDirection(Long id) {

    }


    private Direction getDirectionById(Long id) {
        Direction direction = directionRepository.find(id);
        if (direction == null) {
            throw new DirectionNotFoundException(id);
        }
        return direction;
    }
}
