package com.example.experienceexchange.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Set;

@Setter
@Getter
public class DirectionDto {

    public interface Create {

    }

    @Null(groups = Create.class)
    private Long id;

    @NotNull(groups = Create.class)
    private String header;

    @NotNull(groups = Create.class)
    private Set<SectionDto> sections;
}
