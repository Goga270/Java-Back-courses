package com.example.experienceexchange.service;

import com.example.experienceexchange.dto.SectionDto;
import com.example.experienceexchange.exception.SectionNotFoundException;
import com.example.experienceexchange.model.Section;
import com.example.experienceexchange.repository.interfaceRepo.ISectionRepository;
import com.example.experienceexchange.service.interfaceService.ISectionService;
import com.example.experienceexchange.util.mapper.SectionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;

@Slf4j
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
        log.debug("Create section");
        Section newSection = sectionMapper.sectionDtoToSection(sectionDto);
        Section section = sectionRepository.save(newSection);
        log.debug("Created section {}", section.getId());
        return sectionMapper.sectionToSectionDto(section);
    }

    @Transactional
    @Override
    public SectionDto editSection(Long id, SectionDto sectionDto) {
        log.debug("Edit section {}", id);
        Section section = sectionMapper.sectionDtoToSection(sectionDto);
        section.setId(id);
        sectionRepository.update(section);
        log.debug("Updated section {}", section.getId());
        return sectionMapper.sectionToSectionDto(section);
    }

    @Transactional
    @Override
    public void deleteSection(Long id) {
        log.debug("Delete section {}", id);
        try {
            sectionRepository.deleteById(id);
            log.debug("Section {} deleted", id);
        } catch (EntityExistsException exception) {
            log.warn("Section {} is not found", id);
            throw new SectionNotFoundException(id);
        }
    }


    private Section getSectionById(Long id) throws SectionNotFoundException {
        Section section = sectionRepository.find(id);
        if (section == null) {
            log.warn("Section {} is not found", id);
            throw new SectionNotFoundException(id);
        }
        return section;
    }
}
