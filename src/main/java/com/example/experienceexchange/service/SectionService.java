package com.example.experienceexchange.service;

import com.example.experienceexchange.dto.SectionDto;
import com.example.experienceexchange.repository.interfaceRepo.ISectionRepository;
import com.example.experienceexchange.service.interfaceService.ISectionService;
import org.springframework.stereotype.Service;

@Service
public class SectionService implements ISectionService {

    private final ISectionRepository sectionRepository;

    public SectionService(ISectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Override
    public void createSection(SectionDto sectionDto) {

    }

    @Override
    public SectionDto editSection(Long id, SectionDto sectionDto) {
        return null;
    }

    @Override
    public void deleteSection(Long id) {

    }
}
