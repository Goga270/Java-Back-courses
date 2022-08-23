package com.example.experienceexchange.controller;

import com.example.experienceexchange.dto.AccountDto;
import com.example.experienceexchange.dto.LessonDto;
import com.example.experienceexchange.dto.NewPasswordDto;
import com.example.experienceexchange.security.JwtUserDetails;
import com.example.experienceexchange.service.interfaceService.IUserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class UserController {

    private final IUserService accountService;

    public UserController(IUserService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/profile")
    public AccountDto getCurrentProfile(@AuthenticationPrincipal JwtUserDetails userDetails) {
        return accountService.getAccount(userDetails);
    }

    @GetMapping("/schedule")
    public Set<LessonDto> getSchedule(@AuthenticationPrincipal JwtUserDetails userDetails) {
        return accountService.getSchedule(userDetails);
    }

    @PostMapping("/profile-settings")
    public AccountDto editProfile(@AuthenticationPrincipal JwtUserDetails userDetails,
                                  @RequestBody @Validated AccountDto accountDto) {
        return accountService.editAccount(userDetails, accountDto);
    }

    @PatchMapping("/profile-settings/password")
    public void changePassword(@AuthenticationPrincipal JwtUserDetails jwtUserDetails,
                               @RequestBody @Validated NewPasswordDto passwordDto) {
        accountService.changePassword(jwtUserDetails, passwordDto);
    }
}
