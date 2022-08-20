package com.example.experienceexchange.controller;

import com.example.experienceexchange.dto.AccountDto;
import com.example.experienceexchange.dto.LoginDto;
import com.example.experienceexchange.dto.TokenDto;
import com.example.experienceexchange.service.IAuthService;
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

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public TokenDto login(@RequestBody @Validated LoginDto loginDto) {
        return authService.authentication(loginDto);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/registration")
    public void registration(@RequestBody @Validated({AccountDto.Registration.class}) AccountDto registrationDto) {
        authService.registrationUser(registrationDto);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/registration-admin")
    public void registrationAdmin(@RequestBody @Validated({AccountDto.Registration.class}) AccountDto registrationDto) {
        authService.registrationAdmin(registrationDto);
    }
}
