package com.example.experienceexchange.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.JoinColumn;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class PaymentDto {

    public interface Create {

    }

    public interface DetailsForPayLesson {

    }

    public interface DetailsForPayCourse {

    }

    @Null(groups = {Create.class})
    private Long id;
    @JsonView({DetailsForPayCourse.class})
    private Long courseId;
    @JsonView({DetailsForPayLesson.class})
    private Long lessonId;
    @JsonView({DetailsForPayCourse.class, DetailsForPayLesson.class})
    @Null(groups = {Create.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss X")
    private Date datePayment;

    @JsonView({DetailsForPayCourse.class, DetailsForPayLesson.class})
    @NotNull(groups = {Create.class})
    private BigDecimal price;

    @JsonView({DetailsForPayCourse.class, DetailsForPayLesson.class})
    private String numberCardUser;

    @JsonView({DetailsForPayCourse.class, DetailsForPayLesson.class})
    private String emailUser;
}
