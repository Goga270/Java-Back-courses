package com.example.experienceexchange.service.interfaceService;

import com.example.experienceexchange.dto.LoginDto;
import com.example.experienceexchange.dto.TokenDto;
import com.example.experienceexchange.dto.UserDto;

public interface IAuthService {

    TokenDto authentication(LoginDto loginDto);

    void registrationUser(UserDto registrationDto);

    void registrationAdmin(UserDto registrationDto);

    void blockUser(Long id);

    void unblockUser(Long id);
}
