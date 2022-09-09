package com.example.experienceexchange.controller;

import com.example.experienceexchange.dto.LoginDto;
import com.example.experienceexchange.dto.TokenDto;
import com.example.experienceexchange.dto.UserDto;
import com.example.experienceexchange.service.interfaceService.IAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public TokenDto login(@RequestBody @Validated LoginDto loginDto) {
        return authService.authentication(loginDto);
    }

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public void registration(@RequestBody @Validated({UserDto.Registration.class}) UserDto registrationDto) {
        authService.registrationUser(registrationDto);
    }

    @PostMapping("/registration-admin")
    @ResponseStatus(HttpStatus.CREATED)
    public void registrationAdmin(@RequestBody @Validated({UserDto.Registration.class}) UserDto registrationDto) {
        authService.registrationAdmin(registrationDto);
    }

    @PatchMapping("/block-user")
    @ResponseStatus(HttpStatus.OK)
    public void blockUser(@RequestParam(name = "id") Long id) {
        authService.blockUser(id);
    }
}
