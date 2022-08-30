package com.example.experienceexchange.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkillDto {

    private Long id;

    @JsonView({CourseDto.Details.class})
    private String name;
}
