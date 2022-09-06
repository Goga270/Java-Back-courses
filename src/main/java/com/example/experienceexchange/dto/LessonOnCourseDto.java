package com.example.experienceexchange.dto;

import com.example.experienceexchange.model.Course;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;
// TODO: ПРОВЕРИТЬ МАППЕРЫ ЧТОБЫ ЛИШНЕГО НЕ ГЕНЕРИРОВАЛИ
@Getter
@Setter
public class LessonOnCourseDto {

    public interface Create {

    }

    public interface DetailsForTimetable{

    }


    @Null(groups = {Create.class})
    private Long id;
    @JsonView({DetailsForTimetable.class})
    @NotNull(groups = {Create.class})
    private String name;

    @NotNull(groups = {Create.class})
    private String description;

    private String homeworkLink;

    private String linkVideo;

    @JsonView({DetailsForTimetable.class})
    @NotNull(groups = {Create.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss X")
    private Date startLesson;

    @JsonView({DetailsForTimetable.class})
    @NotNull(groups = {Create.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss X")
    private Date endLesson;

    @JsonView({DetailsForTimetable.class})
    @NotNull(groups = {Create.class})
    private Integer accessDuration;

    @JsonView({DetailsForTimetable.class})
    private Long courseId;
}
