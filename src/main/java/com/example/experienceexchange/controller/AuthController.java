package com.example.experienceexchange.controller;

import com.example.experienceexchange.dto.LoginDto;
import com.example.experienceexchange.dto.TokenDto;
import com.example.experienceexchange.dto.UserDto;
import com.example.experienceexchange.service.interfaceService.IAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Аутентификация и авторизация пользователей
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final IAuthService authService;
    private UserDto registrationDto;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    /**
     * Аутентификация пользователя в приложении
     * @param loginDto Пароль и логин пользователя
     * @return Токен для авторизации пользователя и почта пользователя
     */
    @PostMapping("/login")
    public TokenDto login(@RequestBody @Validated LoginDto loginDto) {
        return authService.authentication(loginDto);
    }

    /**
     * Регистрация пользователя
     * @param registrationDto Данные пользователя для регистрации
     */
    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public void registration(@RequestBody @Validated({UserDto.Registration.class}) UserDto registrationDto) {
        this.registrationDto = registrationDto;
        authService.registrationUser(registrationDto);
    }

    /**
     * Регистрация администратора
     * @param registrationDto Данные администратора для регистрации
     */
    @PostMapping("/registration-admin")
    @ResponseStatus(HttpStatus.CREATED)
    public void registrationAdmin(@RequestBody @Validated({UserDto.Registration.class}) UserDto registrationDto) {
        authService.registrationAdmin(registrationDto);
    }

    /**
     * Заблокировать пользователя
     * @param id Идентификатор пользователя
     */
    @PatchMapping("/block-user")
    @ResponseStatus(HttpStatus.OK)
    public void blockUser(@RequestParam(name = "id") Long id) {
        authService.blockUser(id);
    }

    /**
     * Разблокировать пользователя
     * @param id Идентификатор пользователя
     */
    @PatchMapping("/unblock-user")
    @ResponseStatus(HttpStatus.OK)
    public void unblockUser(@RequestParam(name = "id") Long id) {
        authService.unblockUser(id);
    }

    @GetMapping("/user-by-email")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserByEmail(@RequestParam("email") String email) {
        return authService.getUserByEmail(email);
    }
}
