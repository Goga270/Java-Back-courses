package com.example.experienceexchange.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
public class NewPasswordDto {

    @NotNull
    private String newPassword;

    @NotNull
    private String newPasswordSecond;
}
