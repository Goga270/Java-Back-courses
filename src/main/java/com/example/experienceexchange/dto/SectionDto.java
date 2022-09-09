package com.example.experienceexchange.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Getter
@Setter
public class SectionDto {

    @JsonView({AdminDetails.class,CourseDto.Details.class, LessonDto.Details.class})
    @Null(groups = {Create.class, DirectionDto.Create.class})
    private Long id;

    @JsonView({AdminDetails.class,CourseDto.Details.class, LessonDto.Details.class})
    @NotNull(groups = {Create.class, Edit.class, DirectionDto.Create.class})
    private String name;

    @JsonView(AdminDetails.class)
    @NotNull(groups = {Create.class, Edit.class})
    private Long directionId;

    public interface Create {
    }

    public interface Edit {
    }

    public interface AdminDetails {
    }
}
