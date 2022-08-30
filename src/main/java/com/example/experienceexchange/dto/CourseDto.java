package com.example.experienceexchange.dto;

import com.example.experienceexchange.model.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class CourseDto {
    // TODO: mapper + параметры

    public interface Create {
    }

    public interface Details {
    }

    public interface Edit {
    }

    @JsonView({Details.class})
    @Null(groups = {Create.class})
    @Null(groups = {Edit.class})
    private Long id;

    @JsonView({Details.class})
    @NotNull(groups = {Create.class})
    private String name;

    @JsonView({Details.class})
    @NotNull(groups = {Create.class})
    private String description;

    @JsonView({Details.class})
    @NotNull(groups = {Create.class})
    @Min(groups = {Create.class}, value = 1)
    @Max(groups = {Create.class}, value = 5)
    private Integer skillLevel;

    @JsonView({Details.class})
    @Null(groups = {Create.class})
    private Long authorId;

    @JsonView({Details.class})
    @NotNull(groups = {Create.class})
    private Integer maxNumberUsers;

    @JsonView({Details.class})
    @Null(groups = {Create.class})
    private Integer currentNumberUsers;

    @JsonView({Details.class})
    @NotNull(groups = {Create.class})
    private BigDecimal price;

    @JsonView({Details.class})
    @NotNull(groups = {Create.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateStart;

    @JsonView({Details.class})
    @NotNull(groups = {Create.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateEnd;

    @JsonView({Details.class})
    private Set<SectionDto> sections;

    @JsonView({Details.class})
    private Set<DirectionDto> directions;

    @JsonView({Details.class})
    private Set<SkillDto> skills;


    private Set<LessonOnCourseDto> lessons;
}
