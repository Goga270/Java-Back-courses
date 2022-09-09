package com.example.experienceexchange.service.interfaceService;

import com.example.experienceexchange.dto.SectionDto;

public interface ISectionService {

    SectionDto createSection(SectionDto sectionDto);

    SectionDto editSection(Long id, SectionDto sectionDto);

    void deleteSection(Long id);
}
