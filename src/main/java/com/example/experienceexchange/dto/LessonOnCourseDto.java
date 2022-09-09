package com.example.experienceexchange.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;

@Getter
@Setter
public class LessonOnCourseDto {

    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss X";

    @Null(groups = {Create.class})
    private Long id;

    @JsonView({DetailsForTimetable.class, DetailsForSubscribe.class})
    @NotNull(groups = {Create.class})
    private String name;

    @JsonView({DetailsForSubscribe.class})
    @NotNull(groups = {Create.class})
    private String description;

    @JsonView({DetailsForSubscribe.class})
    private String homeworkLink;

    @JsonView({DetailsForSubscribe.class})
    private String linkVideo;

    @JsonView({DetailsForTimetable.class, DetailsForSubscribe.class})
    @NotNull(groups = {Create.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
    private Date startLesson;

    @JsonView({DetailsForTimetable.class, DetailsForSubscribe.class})
    @NotNull(groups = {Create.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
    private Date endLesson;

    @JsonView({DetailsForTimetable.class, DetailsForSubscribe.class})
    @NotNull(groups = {Create.class})
    private Integer accessDuration;

    @JsonView({DetailsForTimetable.class})
    private Long courseId;

    public interface Create {
    }

    public interface DetailsForTimetable {
    }

    public interface DetailsForSubscribe {
    }
}
