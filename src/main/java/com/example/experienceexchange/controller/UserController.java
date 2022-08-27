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

    @JsonView(UserDto.Details.class)
    @GetMapping("/profile")
    public UserDto getCurrentProfile(@AuthenticationPrincipal JwtUserDetails userDetails) {
        return userService.getAccount(userDetails);
    }
    // TODO: НАПИСАТЬ РАСПИСАНИЕ
    @GetMapping("/schedule")
    public Set<LessonDto> getSchedule(@AuthenticationPrincipal JwtUserDetails userDetails) {
        return userService.getSchedule(userDetails);
    }

    @JsonView(UserDto.Details.class)
    @PutMapping("/profile-settings")
    public UserDto editProfile(@AuthenticationPrincipal JwtUserDetails userDetails,
                               @RequestBody @Validated(UserDto.Edit.class) UserDto userDto) {
        return userService.editAccount(userDetails, userDto);
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
