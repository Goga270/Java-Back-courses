package com.example.experienceexchange.dto;

import com.example.experienceexchange.constant.Role;
import com.example.experienceexchange.constant.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;


@Getter
@Setter
public class UserDto {

    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss X";
    private static final String EMAIL_MESSAGE = "email invalid";
    private static final String EMAIL_PATTERN = "^(.+)@(\\S+)$";

    @NotNull(groups = {Registration.class})
    @Null(groups = {Edit.class})
    @Email(groups = {Registration.class}, message = EMAIL_MESSAGE, regexp = EMAIL_MESSAGE)
    @JsonView(Details.class)
    private String email;

    @NotNull(groups = {Registration.class, Edit.class})
    @JsonView(Details.class)
    private String lastname;

    @NotNull(groups = {Registration.class, Edit.class})
    @JsonView(Details.class)
    private String firstname;

    @NotNull(groups = {Registration.class, Edit.class})
    @JsonView(Details.class)
    private String patronymic;

    @NotNull(groups = {Registration.class, Edit.class})
    @JsonView(Details.class)
    private String numberPhone;

    @NotNull(groups = {Registration.class, Edit.class})
    @JsonView(Details.class)
    private Integer age;

    @NotNull(groups = {Registration.class, Edit.class})
    @JsonView(Details.class)
    private String numberCard;

    @NotNull(groups = {Registration.class})
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonView(Details.class)
    @Null(groups = {Edit.class})
    private String password;

    @JsonView({Details.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
    private Date created;

    private Date updated;

    @Null(groups = {Registration.class, Edit.class})
    private Role role;

    private Status status;

    public interface Registration {
    }

    public interface Edit {
    }

    public interface Details {
    }
}
