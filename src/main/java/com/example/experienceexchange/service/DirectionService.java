package com.example.experienceexchange.service;

import com.example.experienceexchange.dto.DirectionDto;
import com.example.experienceexchange.exception.DirectionNotFoundException;
import com.example.experienceexchange.model.Direction;
import com.example.experienceexchange.model.Section;
import com.example.experienceexchange.repository.interfaceRepo.IDirectionRepository;
import com.example.experienceexchange.service.interfaceService.IDirectionService;
import com.example.experienceexchange.util.mapper.DirectionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class DirectionService implements IDirectionService {

    private final IDirectionRepository directionRepository;
    private final DirectionMapper directionMapper;

    public DirectionService(IDirectionRepository directionRepository, DirectionMapper directionMapper) {
        this.directionRepository = directionRepository;
        this.directionMapper = directionMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public List<DirectionDto> getAllDirections() {
        log.debug("Get directions");
        List<Direction> directions = directionRepository.findAll();
        return directionMapper.toDirectionDto(directions);
    }

    @Transactional(readOnly = true)
    @Override
    public DirectionDto getDirection(Long directionId) {
        log.debug("Get direction {}", directionId);
        Direction direction = getDirectionById(directionId);
        return directionMapper.directionToDirectionDto(direction);
    }

    @Transactional
    @Override
    public DirectionDto createDirection(DirectionDto directionDto) {
        log.debug("Create new direction with name {}", directionDto.getHeader());
        Direction direction = directionMapper.directionDtoToDirection(directionDto);

        direction.getSections()
                .forEach(section -> section.setDirection(direction));

        Direction save = directionRepository.save(direction);
        log.debug("Created new direction {}", direction.getId());
        return directionMapper.directionToDirectionDto(save);
    }

    @Transactional
    @Override
    public DirectionDto editDirection(Long id, DirectionDto directionDto) {
        log.debug("Editing direction {}", id);

        if (!directionRepository.exists(id)) {
            log.warn("Direction {} not found", id);
            throw new DirectionNotFoundException(id);
        }

        Direction direction = directionMapper.directionDtoToDirection(directionDto);
        direction.setId(id);

        direction.getSections()
                .forEach(section -> section.setDirection(direction));

        Direction updateDirection = directionRepository.update(direction);
        log.debug("Updated direction {}", id);
        return directionMapper.directionToDirectionDto(updateDirection);
    }

    @Transactional
    @Override
    public void deleteDirection(Long id) {
        log.debug("Delete direction {}", id);
        if (directionRepository.exists(id)) {
            directionRepository.deleteById(id);
            log.debug("Direction {} deleted", id);
        } else {
            log.warn("Direction {} is not found", id);
            throw new DirectionNotFoundException(id);
        }
    }

    private Direction getDirectionById(Long id) {
        Direction direction = directionRepository.find(id);
        if (direction == null) {
            log.warn("Direction {} is not found", id);
            throw new DirectionNotFoundException(id);
        }
        return direction;
    }
}
