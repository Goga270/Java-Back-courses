package com.example.experienceexchange.service;

import com.example.experienceexchange.exception.DirectionNotFoundException;
import com.example.experienceexchange.exception.SectionNotFoundException;
import com.example.experienceexchange.model.Direction;
import com.example.experienceexchange.model.Section;
import com.example.experienceexchange.repository.interfaceRepo.IDirectionRepository;
import com.example.experienceexchange.dto.DirectionDto;
import com.example.experienceexchange.service.interfaceService.IDirectionService;
import com.example.experienceexchange.util.mapper.DirectionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Set;

@Service
public class DirectionService implements IDirectionService {

    private final IDirectionRepository directionRepository;
    private final DirectionMapper directionMapper;

    public DirectionService(IDirectionRepository directionRepository, DirectionMapper directionMapper) {
        this.directionRepository = directionRepository;
        this.directionMapper = directionMapper;
    }

    @Transactional
    @Override
    public List<DirectionDto> getAllDirections() {
        List<Direction> directions = directionRepository.findAll();
        return directionMapper.toDirectionDto(directions);
    }

    @Transactional
    @Override
    public DirectionDto getDirection(Long id) {
        Direction direction = getDirectionById(id);
        return directionMapper.directionToDirectionDto(direction);
    }

    @Transactional
    @Override
    public DirectionDto createDirection(DirectionDto directionDto) {
        Direction direction = directionMapper.directionDtoToDirection(directionDto);
        Set<Section> sections = direction.getSections();
        for (Section section : sections) {
            section.setDirection(direction);
        }

        Direction save = directionRepository.save(direction);
        return directionMapper.directionToDirectionDto(save);
    }

    @Transactional
    @Override
    public DirectionDto editDirection(Long id, DirectionDto directionDto) {
        Direction direction = directionMapper.directionDtoToDirection(directionDto);
        direction.setId(id);

        Set<Section> sections = direction.getSections();
        for (Section section : sections) {
            section.setDirection(direction);
        }

        directionRepository.update(direction);
        return directionMapper.directionToDirectionDto(direction);
    }

    @Transactional
    @Override
    public void deleteDirection(Long id) {
        try {
            directionRepository.deleteById(id);
        } catch (EntityExistsException exception) {
            throw new DirectionNotFoundException(id);
        }
    }

    private Direction getDirectionById(Long id) {
        Direction direction = directionRepository.find(id);
        if (direction == null) {
            throw new DirectionNotFoundException(id);
        }
        return direction;
    }
}
