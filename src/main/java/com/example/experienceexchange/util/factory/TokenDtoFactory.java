package com.example.experienceexchange.util.factory;

import com.example.experienceexchange.dto.TokenDto;

public class TokenDtoFactory {

    public static TokenDto createTokenDto(String token, String email) {
        TokenDto tokenDto = new TokenDto();
        tokenDto.setToken(token);
        tokenDto.setEmail(email);
        return tokenDto;
    }
}
