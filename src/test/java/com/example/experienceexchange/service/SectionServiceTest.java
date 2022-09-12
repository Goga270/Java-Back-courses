package com.example.experienceexchange.service;

import com.example.experienceexchange.dto.SectionDto;
import com.example.experienceexchange.exception.SectionNotFoundException;
import com.example.experienceexchange.model.Section;
import com.example.experienceexchange.repository.interfaceRepo.ISectionRepository;
import com.example.experienceexchange.util.mapper.SectionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SectionServiceTest {

    private static final Long SECTION_ID = 1L;
    private static final boolean TRUE_SECTION_EXIST = true;
    private static final boolean FALSE_SECTION_EXIST = false;

    @InjectMocks
    private SectionService sectionService;
    @Mock
    private ISectionRepository sectionRepository;
    @Mock
    private SectionMapper sectionMapper;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_saveNewSectionInDatabase() {
        SectionDto enteredSectionDto = mock(SectionDto.class);
        Section newSection = mock(Section.class);
        SectionDto expectedSectionDto = mock(SectionDto.class);
        when(sectionMapper.sectionDtoToSection(enteredSectionDto)).thenReturn(newSection);
        when(sectionRepository.save(newSection)).thenReturn(newSection);
        when(sectionMapper.sectionToSectionDto(newSection)).thenReturn(expectedSectionDto);

        SectionDto actual = sectionService.createSection(enteredSectionDto);

        assertNotNull(actual);
        assertEquals(expectedSectionDto, actual);
    }

    @Test
    void should_editSection_if_enteredSectionIdCorrect() {
        SectionDto enteredSectionDto = mock(SectionDto.class);
        Section sectionToEdit = mock(Section.class);
        SectionDto expectedSectionDto = mock(SectionDto.class);
        when(sectionMapper.sectionDtoToSection(enteredSectionDto)).thenReturn(sectionToEdit);
        when(sectionRepository.update(sectionToEdit)).thenReturn(sectionToEdit);
        when(sectionMapper.sectionToSectionDto(sectionToEdit)).thenReturn(expectedSectionDto);

        SectionDto actual = sectionService.editSection(SECTION_ID, enteredSectionDto);

        assertNotNull(actual);
        assertEquals(expectedSectionDto, actual);
    }

    @Test
    void should_deleteSection_if_enteredSectionIdCorrect() {
        when(sectionRepository.exists(SECTION_ID)).thenReturn(TRUE_SECTION_EXIST);

        sectionService.deleteSection(SECTION_ID);

        verify(sectionRepository).deleteById(SECTION_ID);
    }

    @Test
    void should_throwSectionNotFoundException_if_enteredSectionIdIncorrect() {
        when(sectionRepository.exists(SECTION_ID)).thenReturn(FALSE_SECTION_EXIST);

        SectionNotFoundException exceptionInDeleteSectionMethod = assertThrows(SectionNotFoundException.class, () -> sectionService.deleteSection(SECTION_ID));

        assertEquals(String.format("Section with id %d not found", SECTION_ID), exceptionInDeleteSectionMethod.getMessage());
    }
}