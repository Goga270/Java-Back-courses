package com.example.experienceexchange.service;

import com.example.experienceexchange.dto.AccountDto;
import com.example.experienceexchange.dto.NewPasswordDto;
import com.example.experienceexchange.repository.interfaceRepo.IUserRepository;
import com.example.experienceexchange.security.JwtUserDetails;
import com.example.experienceexchange.service.interfaceService.IUserService;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    private final IUserRepository userRepository;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AccountDto getAccount(JwtUserDetails userDetails) {
        return null;
    }

    @Override
    public AccountDto editAccount(JwtUserDetails userDetails, AccountDto accountDto) {
        return null;
    }

    @Override
    public void changePassword(JwtUserDetails userDetails, NewPasswordDto passwordDto) {

    }
}
