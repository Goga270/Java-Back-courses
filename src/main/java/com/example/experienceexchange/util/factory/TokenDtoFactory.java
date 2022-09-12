package com.example.experienceexchange.util.factory;

import com.example.experienceexchange.dto.TokenDto;
import org.springframework.stereotype.Component;

@Component
public class TokenDtoFactory {

    public TokenDto createTokenDto(String token, String email) {
        TokenDto tokenDto = new TokenDto();
        tokenDto.setToken(token);
        tokenDto.setEmail(email);
        return tokenDto;
    }
}
