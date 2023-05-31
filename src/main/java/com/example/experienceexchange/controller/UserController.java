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

    /**
     * Достать подписки пользователя на уроки
     * @param userDetails Информация о пользователе
     * @return Найденные подписки на уроки
     */
    @GetMapping("/profile/lessons-subscriptions")
    public List<LessonDto> getLessonsSubscription(@AuthenticationPrincipal JwtUserDetails userDetails) {
        return userService.getLessonsSubscribedByUser(userDetails);
    }

    /**
     * Достать подписки пользователя на курсы
     * @param userDetails Информация о пользователе
     * @return Найденные подписки на курсы
     */
    @GetMapping("/profile/courses-subscriptions")
    public List<CourseDto> getCoursesSubscription(@AuthenticationPrincipal JwtUserDetails userDetails) {
        return userService.getCoursesSubscribedByUser(userDetails);
    }

    /**
     * Достать созданные пользователем курсы
     * @param userDetails Информация о пользователе
     * @return Найденные курсы пользователя
     */
    @GetMapping("profile/created-courses")
    public List<CourseDto> getCreatedCoursesByUser(@AuthenticationPrincipal JwtUserDetails userDetails) {
        return userService.getCreatedCoursesByUser(userDetails);
    }

    /**
     * Достать созданные пользователем уроки
     * @param userDetails Информация о пользователе
     * @return Найденные уроки пользователя
     */
    @GetMapping("profile/created-lessons")
    public List<LessonDto> getCreatedLessonsByUser(@AuthenticationPrincipal JwtUserDetails userDetails) {
        return userService.getCreatedLessonsByUser(userDetails);
    }

    /**
     * Достать историю оплат пользователя
     * @param userDetails Информация о пользователе
     * @return Оплаты пользователя
     */
    @GetMapping("/profile/payments")
    public List<PaymentDto> getPayments(@AuthenticationPrincipal JwtUserDetails userDetails) {
        return userService.getPayments(userDetails);
    }

    /**
     * Достать профиль пользователя
     * @param userDetails Информация о пользователе
     * @return Профиль пользователя
     */
    @JsonView(UserDto.Details.class)
    @GetMapping("/profile")
    public UserDto getCurrentProfile(@AuthenticationPrincipal JwtUserDetails userDetails) {
        return userService.getAccount(userDetails);
    }

    /**
     * Редактировать профиль
     * @param userDetails Информация о пользователе
     * @param userDto Редактируемые данные пользователем
     * @return От редактируемые данные
     */
    @JsonView(UserDto.Details.class)
    @PostMapping("/profile-settings")
    public UserDto editProfile(
            @AuthenticationPrincipal JwtUserDetails userDetails,
            @RequestBody @Validated(UserDto.Edit.class) UserDto userDto) {
        return userService.editAccount(userDetails, userDto);
    }

    /**
     * Изменить пароль пользователя
     * @param jwtUserDetails Информация о пользователе
     * @param passwordDto Новый и старый пароль
     */
    @PostMapping("/profile-settings/password")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails,
            @RequestBody @Validated NewPasswordDto passwordDto) {
        userService.changePassword(jwtUserDetails, passwordDto);
    }

    /**
     * Изменить почту пользователя
     * @param jwtUserDetails Информация о пользователе
     * @param newEmailDto Новая почта пользователя
     */
    @PostMapping("/profile-settings/email")
    @ResponseStatus(HttpStatus.OK)
    public void changeEmail(
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails,
            @RequestBody @Validated NewEmailDto newEmailDto) {
        userService.changeEmail(jwtUserDetails, newEmailDto);
    }
}
