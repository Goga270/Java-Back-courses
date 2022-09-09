package com.example.experienceexchange.controller;

import com.example.experienceexchange.dto.DirectionDto;
import com.example.experienceexchange.dto.SectionDto;
import com.example.experienceexchange.service.interfaceService.IDirectionService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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
    public DirectionDto getDirection(@PathVariable Long id) {
        return directionService.getDirection(id);
    }

    @PostMapping("/new-direction")
    @ResponseStatus(HttpStatus.CREATED)
    public DirectionDto createDirection(@RequestBody @Validated({DirectionDto.Create.class}) DirectionDto directionDto) {
        return directionService.createDirection(directionDto);
    }

    @PutMapping("/{id}/settings")
    public DirectionDto editDirection(@PathVariable Long id,
                                      @RequestBody @Validated DirectionDto directionDto) {
        return directionService.editDirection(id, directionDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteDirection(@PathVariable Long id) {
        directionService.deleteDirection(id);
    }
}
