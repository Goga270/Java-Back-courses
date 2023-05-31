package com.example.experienceexchange.controller;

import com.example.experienceexchange.dto.DirectionDto;
import com.example.experienceexchange.service.interfaceService.IDirectionService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Работа с направлениями
 */
@RestController
@RequestMapping("/directions")
public class DirectionController {

    private final IDirectionService directionService;

    public DirectionController(IDirectionService directionService) {
        this.directionService = directionService;
    }

    /**
     * Достать все направления
     * @return Найденные направления
     */
    @GetMapping("")
    public List<DirectionDto> getAllDirection() {
        return directionService.getAllDirections();
    }

    /**
     * Достать определенное направление
     * @param directionId Идентификатор направления
     * @return Найденное направление
     */
    @GetMapping("/{id}")
    public DirectionDto getDirection(@PathVariable("id") Long directionId) {
        return directionService.getDirection(directionId);
    }

    /**
     * Создать новое направление
     * @param directionDto Информация о направлении
     * @return Новое направление
     */
    @PostMapping("/new-direction")
    @ResponseStatus(HttpStatus.CREATED)
    public DirectionDto createDirection(@RequestBody @Validated({DirectionDto.Create.class}) DirectionDto directionDto) {
        return directionService.createDirection(directionDto);
    }

    /**
     * Редактировать направление
     * @param directionId Идентификатор направления
     * @param directionDto Информация о направлении
     * @return От редактируемое направление
     */
    @PutMapping("/{id}/settings")
    public DirectionDto editDirection(
            @PathVariable("id") Long directionId,
            @RequestBody @Validated DirectionDto directionDto) {
        return directionService.editDirection(directionId, directionDto);
    }

    /**
     * Удалить направление
     * @param directionId Идентификатор направления
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteDirection(@PathVariable("id") Long directionId) {
        directionService.deleteDirection(directionId);
    }
}
