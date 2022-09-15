package com.example.experienceexchange.controller;

import com.example.experienceexchange.dto.DirectionDto;
import com.example.experienceexchange.service.interfaceService.IDirectionService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/directions")
public class DirectionController {

    private final IDirectionService directionService;

    public DirectionController(IDirectionService directionService) {
        this.directionService = directionService;
    }

    @GetMapping("")
    public List<DirectionDto> getAllDirection() {
        return directionService.getAllDirections();
    }

    @GetMapping("/{id}")
    public DirectionDto getDirection(@PathVariable("id") Long directionId) {
        return directionService.getDirection(directionId);
    }

    @PostMapping("/new-direction")
    @ResponseStatus(HttpStatus.CREATED)
    public DirectionDto createDirection(@RequestBody @Validated({DirectionDto.Create.class}) DirectionDto directionDto) {
        return directionService.createDirection(directionDto);
    }

    @PutMapping("/{id}/settings")
    public DirectionDto editDirection(
            @PathVariable("id") Long directionId,
            @RequestBody @Validated DirectionDto directionDto) {
        return directionService.editDirection(directionId, directionDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteDirection(@PathVariable("id") Long directionId) {
        directionService.deleteDirection(directionId);
    }
}
