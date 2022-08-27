package com.example.experienceexchange.dto;

import com.example.experienceexchange.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    public interface Create{}

    @NotNull(groups = {Create.class})
    private String header;

    @NotNull(groups = {Create.class})
    private String body;

    @Null(groups = {Create.class})
    private UserDto authorId;

    @Null(groups = {Create.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss X")
    private Date created;

    @NotNull(groups = {Create.class})
    @Min(value = 1)
    @Max(value = 5)
    private Integer rating;
}
