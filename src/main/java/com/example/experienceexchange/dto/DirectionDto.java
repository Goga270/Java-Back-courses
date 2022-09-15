package com.example.experienceexchange.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Set;

@Setter
@Getter
public class DirectionDto {

    @JsonView({CourseDto.Details.class, LessonDto.Details.class})
    @Null(groups = Create.class)
    private Long id;

    @JsonView({CourseDto.Details.class, LessonDto.Details.class})
    @NotNull(groups = Create.class)
    private String header;

    private Set<SectionDto> sections;

    public interface Create {
    }
}
