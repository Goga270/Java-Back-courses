package com.example.experienceexchange.controller;

import com.example.experienceexchange.dto.*;
import com.example.experienceexchange.security.JwtUserDetails;
import com.example.experienceexchange.service.interfaceService.IUserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @JsonView(AccountDto.Details.class)
    @GetMapping("/profile")
    public AccountDto getCurrentProfile(@AuthenticationPrincipal JwtUserDetails userDetails) {
        return userService.getAccount(userDetails);
    }
    // TODO: НАПИСАТЬ РАСПИСАНИЕ
    @GetMapping("/schedule")
    public Set<LessonDto> getSchedule(@AuthenticationPrincipal JwtUserDetails userDetails) {
        return userService.getSchedule(userDetails);
    }

    @JsonView(AccountDto.Details.class)
    @PutMapping("/profile-settings")
    public AccountDto editProfile(@AuthenticationPrincipal JwtUserDetails userDetails,
                                  @RequestBody @Validated(AccountDto.Edit.class) AccountDto accountDto) {
        return userService.editAccount(userDetails, accountDto);
    }

    @PatchMapping("/profile-settings/password")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@AuthenticationPrincipal JwtUserDetails jwtUserDetails,
                               @RequestBody @Validated NewPasswordDto passwordDto) {
        userService.changePassword(jwtUserDetails, passwordDto);
    }

    @PatchMapping("/profile-settings/email")
    @ResponseStatus(HttpStatus.OK)
    public void changeEmail(@AuthenticationPrincipal JwtUserDetails jwtUserDetails,
                               @RequestBody @Validated NewEmailDto newEmailDto) {
        userService.changeEmail(jwtUserDetails, newEmailDto);
    }
}
