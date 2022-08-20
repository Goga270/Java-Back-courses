package com.example.experienceexchange.service.interfaceService;

import com.example.experienceexchange.dto.AccountDto;
import com.example.experienceexchange.dto.NewPasswordDto;
import com.example.experienceexchange.security.JwtUserDetails;

public interface IUserService {

    AccountDto getAccount(JwtUserDetails userDetails);

    AccountDto editAccount(JwtUserDetails userDetails, AccountDto accountDto);

    void changePassword(JwtUserDetails userDetails, NewPasswordDto passwordDto);
}
