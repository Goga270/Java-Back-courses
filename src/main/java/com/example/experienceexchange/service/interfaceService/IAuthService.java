package com.example.experienceexchange.service.interfaceService;

import com.example.experienceexchange.dto.UserDto;
import com.example.experienceexchange.dto.LoginDto;
import com.example.experienceexchange.dto.TokenDto;

public interface IAuthService {

    TokenDto authentication(LoginDto loginDto);

    void registrationUser(UserDto registrationDto);

    void registrationAdmin(UserDto registrationDto);

//    void blockUser( accountDto);/**/ // TODO: ТОГДА НАДО БУДЕТ ПРОВЕРЯТЬ ТОКЕН ЧТОБЫ УЗНАТЬ НЕ ЗАБЛОЧЕН ЛИ АКК, ХОТЯ МОЖЕТ ОН САМ ПРОВЕРИТ СЕБЯ
}
