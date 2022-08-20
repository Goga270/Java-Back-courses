package com.example.experienceexchange.dto;

import com.example.experienceexchange.constant.Role;
import com.example.experienceexchange.constant.Status;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AccountDto {

    public interface Registration {
    }

    @NotNull(groups = {Registration.class})
    @Email(groups = {Registration.class}, message = "email invalid",regexp = "^(.+)@(\\S+)$")
    private String email;
    @NotNull(groups = {Registration.class})
    private String lastName;
    @NotNull(groups = {Registration.class})
    private String firstName;
    @NotNull(groups = {Registration.class})
    private String numberPhone;
    @NotNull(groups = {Registration.class})
    private String password;
    private String created;
    private String updated;
    private Role role;
    private Status status;
}
