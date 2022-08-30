package com.example.experienceexchange.dto;

import com.example.experienceexchange.model.Course;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;

@Getter
@Setter
public class LessonOnCourseDto {

    public interface Create {

    }
    @Null(groups = {Create.class})
    private Long id;

    @NotNull(groups = {Create.class})
    private String name;

    @NotNull(groups = {Create.class})
    private String description;

    private String homeworkLink;

    private String linkVideo;

    @NotNull(groups = {Create.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss X")
    private Date startLesson;

    @NotNull(groups = {Create.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss X")
    private Date endLesson;

    @NotNull(groups = {Create.class})
    private Integer accessDuration;

    private Long courseId;
}
