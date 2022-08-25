package com.example.experienceexchange.service;

import com.example.experienceexchange.dto.SectionDto;
import com.example.experienceexchange.exception.DirectionNotFoundException;
import com.example.experienceexchange.exception.SectionNotFoundException;
import com.example.experienceexchange.model.Direction;
import com.example.experienceexchange.model.Section;
import com.example.experienceexchange.repository.interfaceRepo.IDirectionRepository;
import com.example.experienceexchange.repository.interfaceRepo.ISectionRepository;
import com.example.experienceexchange.service.interfaceService.ISectionService;
import com.example.experienceexchange.util.mapper.SectionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;

@Service
public class SectionService implements ISectionService {

    private final ISectionRepository sectionRepository;
    private final SectionMapper sectionMapper;

    public SectionService(ISectionRepository sectionRepository, SectionMapper sectionMapper) {
        this.sectionRepository = sectionRepository;
        this.sectionMapper = sectionMapper;
    }

    @Transactional
    @Override
    public SectionDto createSection(SectionDto sectionDto) {
        Section newSection = sectionMapper.sectionDtoToSection(sectionDto);
        Section section = sectionRepository.save(newSection);
        return sectionMapper.sectionToSectionDto(section);
    }

    @Transactional
    @Override
    public SectionDto editSection(Long id, SectionDto sectionDto) {
        Section section = sectionMapper.sectionDtoToSection(sectionDto);
        section.setId(id);
        sectionRepository.update(section);
        return sectionMapper.sectionToSectionDto(section);
    }

    @Transactional
    @Override
    public void deleteSection(Long id) {
        try {
            sectionRepository.deleteById(id);
        } catch (EntityExistsException exception) {
            throw new SectionNotFoundException(id);
        }
    }


    private Section getSectionById(Long id) throws SectionNotFoundException {
        Section section = sectionRepository.find(id);
        if (section == null) {
            throw new SectionNotFoundException(id);
        }
        return section;
    }
}
