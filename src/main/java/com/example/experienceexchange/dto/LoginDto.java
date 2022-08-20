package com.example.experienceexchange.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class LoginDto {

    @NotNull
    private String email;
    @NotNull
    private String password;
}
