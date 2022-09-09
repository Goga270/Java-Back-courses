package com.example.experienceexchange.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class PaymentDto {

    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss X";

    @Null(groups = {CreateLesson.class, CreateCourse.class})
    private Long id;

    @JsonView(CreateCourse.class)
    @Null(groups = {CreateLesson.class})
    private Long courseId;

    @JsonView(CreateLesson.class)
    @Null(groups = {CreateCourse.class})
    private Long lessonId;

    @JsonView({CreateLesson.class, CreateCourse.class})
    @Null(groups = {CreateLesson.class, CreateCourse.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
    private Date datePayment;

    @JsonView({CreateLesson.class, CreateCourse.class})
    @NotNull(groups = {CreateLesson.class})
    private BigDecimal price;

    @JsonView({CreateLesson.class, CreateCourse.class})
    @Null(groups = {CreateLesson.class, CreateCourse.class})
    private String numberCardUser;

    @JsonView({CreateLesson.class, CreateCourse.class})
    @Null(groups = {CreateLesson.class, CreateCourse.class})
    private String emailUser;

    public interface CreateLesson {
    }

    public interface CreateCourse {
    }
}
