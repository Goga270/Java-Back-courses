package com.example.experienceexchange.controller;

import com.example.experienceexchange.dto.SectionDto;
import com.example.experienceexchange.service.interfaceService.DirectionDto;
import com.example.experienceexchange.service.interfaceService.ISectionService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/section")
public class SectionController {

    private final ISectionService sectionService;

    public SectionController(ISectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/new-section")
    @ResponseStatus(HttpStatus.CREATED)
    public void createSection(@RequestBody @Validated(SectionDto.Create.class) SectionDto sectionDto) {
        sectionService.createSection(sectionDto);
    }

    @PutMapping("/{id}/settings")
    public SectionDto editSection(@PathVariable Long id, @RequestBody SectionDto sectionDto) {
        return sectionService.editSection(id,sectionDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteSection(@PathVariable Long id) {
        sectionService.deleteSection(id);
    }
}
