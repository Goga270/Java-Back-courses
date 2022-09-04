package com.example.experienceexchange.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;

@Getter
@Setter
public class CommentDto {

    public interface CreateForCourse {
    }

    public interface CreateForLesson {
    }
    @JsonView({CreateForCourse.class, CreateForLesson.class})
    @NotNull(groups = {CreateForCourse.class, CreateForLesson.class})
    private String header;

    @JsonView({CreateForCourse.class, CreateForLesson.class})
    @NotNull(groups = {CreateForCourse.class, CreateForLesson.class})
    private String body;

    @JsonView({CreateForCourse.class, CreateForLesson.class})
    @Null(groups = {CreateForCourse.class, CreateForLesson.class})
    private String authorName;

    @JsonView({CreateForCourse.class})
    @NotNull(groups = {CreateForCourse.class})
    @Null(groups = {CreateForLesson.class})
    private CourseDto course;

    @JsonView({CreateForLesson.class})
    @NotNull(groups = {CreateForLesson.class})
    @Null(groups = {CreateForCourse.class})
    private LessonDto lesson;

    @JsonView({CreateForCourse.class, CreateForLesson.class})
    @Null(groups = {CreateForCourse.class, CreateForLesson.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss X")
    private Date created;

    @JsonView({CreateForCourse.class, CreateForLesson.class})
    @NotNull(groups = {CreateForCourse.class, CreateForLesson.class})
    @Min(groups = {CreateForCourse.class, CreateForLesson.class}, value = 1)
    @Max(groups = {CreateForCourse.class, CreateForLesson.class}, value = 5)
    private Integer rating;
}
