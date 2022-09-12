package com.example.experienceexchange.service;

import com.example.experienceexchange.dto.DirectionDto;
import com.example.experienceexchange.exception.DirectionNotFoundException;
import com.example.experienceexchange.model.Direction;
import com.example.experienceexchange.model.Section;
import com.example.experienceexchange.repository.interfaceRepo.IDirectionRepository;
import com.example.experienceexchange.util.mapper.DirectionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DirectionServiceTest {

    private static final Long DIRECTION_ID = 1L;
    private static final boolean TRUE_EXIST_DIRECTION = true;
    private static final boolean FALSE_EXIST_DIRECTION = false;

    @InjectMocks
    private DirectionService directionService;
    @Mock
    private IDirectionRepository directionRepository;
    @Mock
    private DirectionMapper directionMapper;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_returnAllDirectionsDto() {
        List<Direction> directions = mock(List.class);
        List<DirectionDto> expectedDirections = mock(List.class);
        when(directionRepository.findAll()).thenReturn(directions);
        when(directionMapper.toDirectionDto(directions)).thenReturn(expectedDirections);

        List<DirectionDto> actual = directionService.getAllDirections();

        assertNotNull(actual);
        assertEquals(expectedDirections, actual);
    }

    @Test
    void should_returnDirectionDto_when_enteredDirectionIdCorrect() {
        Direction direction = mock(Direction.class);
        DirectionDto expectedDirection = mock(DirectionDto.class);
        when(directionRepository.find(DIRECTION_ID)).thenReturn(direction);
        when(directionMapper.directionToDirectionDto(direction)).thenReturn(expectedDirection);

        DirectionDto actual = directionService.getDirection(DIRECTION_ID);

        assertNotNull(actual);
        assertEquals(expectedDirection, actual);
    }

    @Test
    void should_throwDirectionNotFound_when_enteredDirectionIdIncorrect() {
        DirectionDto directionDto = mock(DirectionDto.class);
        when(directionRepository.find(DIRECTION_ID)).thenReturn(null);

        DirectionNotFoundException exceptionInGetDirectionMethod = assertThrows(DirectionNotFoundException.class, () -> directionService.getDirection(DIRECTION_ID));
        DirectionNotFoundException exceptionInEditDirectionMethod = assertThrows(DirectionNotFoundException.class, () -> directionService.editDirection(DIRECTION_ID, directionDto));

        assertEquals(String.format("Direction with id %d not found", DIRECTION_ID), exceptionInEditDirectionMethod.getMessage());
        assertEquals(String.format("Direction with id %d not found", DIRECTION_ID), exceptionInGetDirectionMethod.getMessage());
    }


    @Test
    void should_saveDirectionInDatabase_when_enteredCorrectData() {
        DirectionDto enteredDirectionDto = mock(DirectionDto.class);
        Direction direction = mock(Direction.class);
        Set<Section> sections = mock(Set.class);
        DirectionDto expectedDirectionDto = mock(DirectionDto.class);
        when(directionMapper.directionDtoToDirection(enteredDirectionDto)).thenReturn(direction);
        when(direction.getSections()).thenReturn(sections);
        when(directionRepository.save(direction)).thenReturn(direction);
        when(directionMapper.directionToDirectionDto(direction)).thenReturn(expectedDirectionDto);

        DirectionDto actual = directionService.createDirection(enteredDirectionDto);

        assertNotNull(actual);
        assertEquals(expectedDirectionDto, actual);
    }


    @Test
    void should_editDirection_if_enteredDirectionIdCorrect() {
        DirectionDto enteredDirectionDto = mock(DirectionDto.class);
        Direction direction = mock(Direction.class);
        Set<Section> sections = mock(Set.class);
        DirectionDto expectedDirectionDto = mock(DirectionDto.class);
        when(directionRepository.exists(DIRECTION_ID)).thenReturn(TRUE_EXIST_DIRECTION);
        when(directionMapper.directionDtoToDirection(enteredDirectionDto)).thenReturn(direction);
        when(direction.getSections()).thenReturn(sections);
        when(directionRepository.update(direction)).thenReturn(direction);
        when(directionMapper.directionToDirectionDto(direction)).thenReturn(expectedDirectionDto);

        DirectionDto actual = directionService.editDirection(DIRECTION_ID, enteredDirectionDto);

        assertNotNull(actual);
        assertEquals(expectedDirectionDto, actual);
    }

    @Test
    void should_deleteDirection_if_enteredDirectionIdCorrect() {
        when(directionRepository.exists(DIRECTION_ID)).thenReturn(TRUE_EXIST_DIRECTION);

        directionService.deleteDirection(DIRECTION_ID);

        verify(directionRepository).deleteById(DIRECTION_ID);
    }

    @Test
    void should_throwDirectionNotFoundException_if_directionNotExist() {
        when(directionRepository.exists(DIRECTION_ID)).thenReturn(FALSE_EXIST_DIRECTION);

        DirectionNotFoundException exceptionInDeleteDirectionMethod = assertThrows(DirectionNotFoundException.class, () -> directionService.deleteDirection(DIRECTION_ID));

        assertEquals(String.format("Direction with id %d not found", DIRECTION_ID), exceptionInDeleteDirectionMethod.getMessage());
    }
}