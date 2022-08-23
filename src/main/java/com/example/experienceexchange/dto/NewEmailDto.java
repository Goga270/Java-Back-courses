package com.example.experienceexchange.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class NewEmailDto {

    @NotNull
    @Email(message = "email invalid", regexp = "^(.+)@(\\S+)$")
    private String newEmail;
}
