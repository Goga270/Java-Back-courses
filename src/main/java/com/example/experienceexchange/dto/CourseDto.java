package com.example.experienceexchange.dto;

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
import java.util.Set;

@Getter
@Setter
public class CourseDto {

    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss X";
    private static final String MASTERY_LEVEL_MESSAGE = "must be between 1 and 5";

    @JsonView({CommentDto.CreateForCourse.class, Details.class})
    @Null(groups = {Create.class})
    @NotNull(groups = {CommentDto.CreateForCourse.class, Edit.class})
    private Long id;

    @JsonView({Details.class, DetailsForUserProfile.class})
    @NotNull(groups = {Create.class, Edit.class})
    private String name;

    @JsonView({Details.class})
    @NotNull(groups = {Create.class, Edit.class})
    private String description;

    @JsonView({Details.class, DetailsForUserProfile.class})
    @NotNull(groups = {Create.class, Edit.class})
    @Min(groups = {Create.class, Edit.class}, value = 1, message = MASTERY_LEVEL_MESSAGE)
    @Max(groups = {Create.class, Edit.class}, value = 5, message = MASTERY_LEVEL_MESSAGE)
    private Integer masteryLevel;

    @JsonView({Details.class})
    @Null(groups = {Create.class, Edit.class})
    private Long authorId;

    @JsonView({Details.class})
    @NotNull(groups = {Create.class, Edit.class})
    private Integer maxNumberUsers;

    @JsonView({Details.class})
    @Null(groups = {Create.class, Edit.class})
    private Integer currentNumberUsers;

    @JsonView({Details.class})
    @NotNull(groups = {Create.class, Edit.class})
    private BigDecimal price;

    @JsonView({Details.class, DetailsForUserProfile.class})
    @NotNull(groups = {Edit.class, Create.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
    private Date dateStart;

    @JsonView({Details.class, DetailsForUserProfile.class})
    @NotNull(groups = {Edit.class, Create.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
    private Date dateEnd;

    @JsonView({Details.class})
    private Set<SectionDto> sections;

    @JsonView({Details.class})
    private Set<DirectionDto> directions;

    @JsonView({Details.class})
    private Set<SkillDto> skills;

    @JsonView({DetailsForUserProfile.class})
    private Set<LessonOnCourseDto> lessons;

    public interface Create {
    }

    public interface DetailsForUserProfile {
    }

    public interface Details {
    }

    public interface Edit {
    }
}
