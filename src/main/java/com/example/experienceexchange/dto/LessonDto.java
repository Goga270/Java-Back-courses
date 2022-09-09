package com.example.experienceexchange.dto;

import com.example.experienceexchange.constant.TypeLesson;
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
public class LessonDto {

    public interface Create {
    }

    public interface Details {
    }

    public interface Edit {
    }

    public interface DetailsForTimetable {
    }

    public interface DetailsForUserProfile {
    }

    @JsonView({CommentDto.CreateForLesson.class, Details.class})
    @Null(groups = {Create.class})
    @NotNull(groups = {CommentDto.CreateForLesson.class, Edit.class})
    private Long id;

    @JsonView({Details.class, DetailsForTimetable.class, DetailsForUserProfile.class})
    @NotNull(groups = {Create.class, Edit.class})
    private String name;
    @JsonView({Details.class})
    @NotNull(groups = {Create.class, Edit.class})
    private String description;
    @JsonView({Details.class})
    @NotNull(groups = {Create.class, Edit.class})
    private String homeworkLink;
    @JsonView({Details.class})
    @NotNull(groups = {Create.class, Edit.class})
    private String linkVideo;
    @JsonView({Details.class, DetailsForTimetable.class, DetailsForUserProfile.class})
    @NotNull(groups = {Create.class, Edit.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss X")
    private Date startLesson;

    @JsonView({Details.class, DetailsForTimetable.class, DetailsForUserProfile.class})
    @NotNull(groups = {Create.class, Edit.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss X")
    private Date endLesson;

    @JsonView({Details.class, DetailsForUserProfile.class})
    @NotNull(groups = {Create.class, Edit.class})
    @Min(groups = {Create.class, Edit.class}, value = 1, message = "must be between 1 and 5")
    @Max(groups = {Create.class, Edit.class}, value = 5, message = "must be between 1 and 5")
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

    @JsonView({Details.class, DetailsForUserProfile.class})
    @NotNull(groups = {Create.class, Edit.class})
    private BigDecimal price;

    @JsonView({Details.class, DetailsForTimetable.class, DetailsForUserProfile.class})
    @NotNull(groups = {Create.class})
    private TypeLesson typeLesson;

    @JsonView({Details.class})
    private Set<SectionDto> sections = new HashSet<>();

    @JsonView({Details.class})
    private Set<DirectionDto> directions = new HashSet<>();

    @JsonView({Details.class})
    private Set<SkillDto> skills = new HashSet<>();
}
