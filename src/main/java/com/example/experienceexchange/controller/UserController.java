package com.example.experienceexchange.controller;

import com.example.experienceexchange.dto.*;
import com.example.experienceexchange.security.JwtUserDetails;
import com.example.experienceexchange.service.interfaceService.IUserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile/lessons-subscriptions")
    public List<LessonDto> getLessonsSubscription(@AuthenticationPrincipal JwtUserDetails userDetails) {
        return userService.getLessonsSubscribedByUser(userDetails);
    }

    @GetMapping("/profile/courses-subscriptions")
    public List<CourseDto> getCoursesSubscription(@AuthenticationPrincipal JwtUserDetails userDetails) {
        return userService.getCoursesSubscribedByUser(userDetails);
    }

    @GetMapping("profile/created-courses")
    public List<CourseDto> getCreatedCoursesByUser(@AuthenticationPrincipal JwtUserDetails userDetails) {
        return userService.getCreatedCoursesByUser(userDetails);
    }

    @GetMapping("profile/created-lessons")
    public List<LessonDto> getCreatedLessonsByUser(@AuthenticationPrincipal JwtUserDetails userDetails) {
        return userService.getCreatedLessonsByUser(userDetails);
    }

    @GetMapping("/profile/payments")
    public List<PaymentDto> getPayments(@AuthenticationPrincipal JwtUserDetails userDetails) {
        return userService.getPayments(userDetails);
    }

    @JsonView(UserDto.Details.class)
    @GetMapping("/profile")
    public UserDto getCurrentProfile(@AuthenticationPrincipal JwtUserDetails userDetails) {
        return userService.getAccount(userDetails);
    }

    @JsonView(UserDto.Details.class)
    @PutMapping("/profile-settings")
    public UserDto editProfile(
            @AuthenticationPrincipal JwtUserDetails userDetails,
            @RequestBody @Validated(UserDto.Edit.class) UserDto userDto) {
        return userService.editAccount(userDetails, userDto);
    }

    @PatchMapping("/profile-settings/password")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails,
            @RequestBody @Validated NewPasswordDto passwordDto) {
        userService.changePassword(jwtUserDetails, passwordDto);
    }

    @PatchMapping("/profile-settings/email")
    @ResponseStatus(HttpStatus.OK)
    public void changeEmail(
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails,
            @RequestBody @Validated NewEmailDto newEmailDto) {
        userService.changeEmail(jwtUserDetails, newEmailDto);
    }
}
