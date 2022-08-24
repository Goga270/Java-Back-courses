package com.example.experienceexchange.dto;

import com.example.experienceexchange.constant.Role;
import com.example.experienceexchange.constant.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;


@Getter
@Setter
public class AccountDto {
    // TODO : НУЖЕН ЛИ AdminDetails?
    public interface Registration {
    }

    public interface Edit {
    }

    public interface AdminDetails {

    }

    public interface Details {
    }

    @NotNull(groups = {Registration.class, Edit.class})
    @Email(groups = {Registration.class}, message = "email invalid", regexp = "^(.+)@(\\S+)$")
    @JsonView(Details.class)
    private String email;

    @NotNull(groups = {Registration.class, Edit.class})
    @JsonView(Details.class)
    private String lastName;

    @NotNull(groups = {Registration.class, Edit.class})
    @JsonView(Details.class)
    private String firstName;

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
    private String password;

    @JsonView(AdminDetails.class)
    private String created;

    @JsonView(AdminDetails.class)
    private String updated;

    @JsonView(AdminDetails.class)
    private Role role;

    @JsonView(AdminDetails.class)
    private Status status;
}
