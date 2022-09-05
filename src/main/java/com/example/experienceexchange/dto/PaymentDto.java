package com.example.experienceexchange.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class PaymentDto {

    public interface CreateLesson {

    }

    public interface CreateCourse {

    }

    @Null(groups = {CreateLesson.class, CreateCourse.class})
    private Long id;

    @NotNull(groups = {CreateCourse.class})
    @Null(groups = {CreateLesson.class})
    private Long courseId;

    @NotNull(groups = {CreateLesson.class})
    @Null(groups = {CreateCourse.class})
    private Long lessonId;

    @Null(groups = {CreateLesson.class, CreateCourse.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss X")
    private Date datePayment;

    @NotNull(groups = {CreateLesson.class})
    private BigDecimal price;

    @Null(groups = {CreateLesson.class, CreateCourse.class})
    private String numberCardUser;

    @Null(groups = {CreateLesson.class, CreateCourse.class})
    private String emailUser;
}
