package com.example.experienceexchange.security.userDetailsService;

import com.example.experienceexchange.model.User;
import com.example.experienceexchange.repository.interfaceRepo.IUserRepository;
import com.example.experienceexchange.util.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final String NOT_FOUND_USER = "User with email %s not found";

    private final IUserRepository userRepository;
    private final UserMapper userMapper;

    public UserDetailsServiceImpl(IUserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User account = userRepository.findByEmail(username);

        if (account == null) {
            throw new UsernameNotFoundException(String.format(NOT_FOUND_USER, username));
        }

        return userMapper.UserToUserDetails(account);

    }
}
