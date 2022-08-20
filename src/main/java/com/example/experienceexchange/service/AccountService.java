package com.example.experienceexchange.service;

import com.example.experienceexchange.repository.IUserRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final IUserRepository userRepository;

    public AccountService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
