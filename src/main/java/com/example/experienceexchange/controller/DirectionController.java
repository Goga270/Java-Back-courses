package com.example.experienceexchange.controller;

import com.example.experienceexchange.dto.CourseDto;
import com.example.experienceexchange.dto.LessonDto;
import com.example.experienceexchange.service.interfaceService.DirectionDto;
import com.example.experienceexchange.service.interfaceService.IDirectionService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/direction")
public class DirectionController {
    // TODO: СДЕЛАТЬ ФУЛЛ ТОЛЬКО ДЛЯ АДМИНА
    private final IDirectionService directionService;

    public DirectionController(IDirectionService directionService) {
        this.directionService = directionService;
    }

    @PostMapping("new-direction")
    @ResponseStatus(HttpStatus.CREATED)
    public void createDirection(@RequestBody @Validated(DirectionDto.Create.class) DirectionDto directionDto) {
        directionService.createDirection(directionDto);
    }

    @PutMapping("{id}/settings")
    public DirectionDto editDirection(@PathVariable Long id, @RequestBody DirectionDto directionDto) {
        return directionService.editDirection(id,directionDto);
    }

    @DeleteMapping("{id}")
    public void deleteDirection(@PathVariable Long id) {
        directionService.deleteDirection(id);
    }
}
