package com.example.experienceexchange.service.interfaceService;

import com.example.experienceexchange.dto.AccountDto;
import com.example.experienceexchange.dto.LessonDto;
import com.example.experienceexchange.dto.NewEmailDto;
import com.example.experienceexchange.dto.NewPasswordDto;
import com.example.experienceexchange.model.User;
import com.example.experienceexchange.security.JwtUserDetails;

import java.util.Set;

public interface IUserService {

    AccountDto getAccount(JwtUserDetails userDetails);

    AccountDto editAccount(JwtUserDetails userDetails, AccountDto accountDto);

    void changePassword(JwtUserDetails userDetails, NewPasswordDto passwordDto);

    Set<LessonDto> getSchedule(JwtUserDetails userDetails);

    User getUserById(Long id);

    void changeEmail(JwtUserDetails jwtUserDetails, NewEmailDto newEmailDto);
}
