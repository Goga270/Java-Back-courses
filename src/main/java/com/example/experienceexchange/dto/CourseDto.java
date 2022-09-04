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

    @JsonView({CommentDto.CreateForCourse.class, Details.class})
    @Null(groups = {Create.class})
    @NotNull(groups = {CommentDto.CreateForCourse.class, Edit.class})
    private Long id;

    @JsonView({Details.class})
    @NotNull(groups = {Create.class, Edit.class})
    private String name;

    @JsonView({Details.class})
    @NotNull(groups = {Create.class, Edit.class})
    private String description;

    @JsonView({Details.class})
    @NotNull(groups = {Create.class, Edit.class})
    @Min(groups = {Create.class, Edit.class}, value = 1, message = "must be between 1 and 5")
    @Max(groups = {Create.class, Edit.class}, value = 5, message = "must be between 1 and 5")
    private Integer skillLevel;

    @JsonView({Details.class})
    @Null(groups = {Create.class, Edit.class})
    private Long authorId;

    @JsonView({Details.class})
    @NotNull(groups = {Create.class, Edit.class})
    private Integer maxNumberUsers;

    @JsonView({Details.class})
    @Null(groups = {Create.class, Edit.class})
    private Integer currentNumberUsers;
        // TODO : МОЖНО ЛИ МЕНЯТЬ ПРАЙС (ИЗМЕНИТЬСЯ У КЛИЕНТОВ)
    @JsonView({Details.class})
    @NotNull(groups = {Create.class, Edit.class})
    private BigDecimal price;

    // TODO : МОЖНО МЕНЯТЬ ТОЛЬКО ВО ВРЕМЯ ПОКА КУРС НЕ НАЧАЛСЯ ИНАЧЕ НЕЛЬЗЯ
    @JsonView({Details.class})
    @NotNull(groups = {Edit.class, Create.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateStart;

    // TODO : МОЖНО МЕНЯТЬ ТОЛЬКО ВО ВРЕМЯ ПОКА КУРС НЕ НАЧАЛСЯ ИНАЧЕ НЕЛЬЗЯ
    @JsonView({Details.class})
    @NotNull(groups = {Edit.class, Create.class})
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
