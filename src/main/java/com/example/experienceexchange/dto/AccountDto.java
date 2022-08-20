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

    public interface Registration {
    }

    public interface AdminDetails {

    }

    @NotNull(groups = {Registration.class})
    @Email(groups = {Registration.class}, message = "email invalid", regexp = "^(.+)@(\\S+)$")
    private String email;

    @NotNull(groups = {Registration.class})
    private String lastName;

    @NotNull(groups = {Registration.class})
    private String firstName;

    @NotNull(groups = {Registration.class})
    private String patronymic;

    @NotNull(groups = {Registration.class})
    private String numberPhone;

    @NotNull(groups = {Registration.class})
    private Integer age;

    @NotNull(groups = {Registration.class})
    private String numberCard;

    @NotNull(groups = {Registration.class})
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
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
