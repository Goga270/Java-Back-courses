package com.example.experienceexchange.service;

import com.example.experienceexchange.constant.Role;
import com.example.experienceexchange.constant.Status;
import com.example.experienceexchange.dto.AccountDto;
import com.example.experienceexchange.dto.LoginDto;
import com.example.experienceexchange.dto.TokenDto;
import com.example.experienceexchange.exception.EmailNotUniqueException;
import com.example.experienceexchange.model.User;
import com.example.experienceexchange.repository.interfaceRepo.IUserRepository;
import com.example.experienceexchange.security.JwtUserDetails;
import com.example.experienceexchange.security.provider.IJwtTokenProvider;
import com.example.experienceexchange.service.interfaceService.IAuthService;
import com.example.experienceexchange.util.factory.TokenDtoFactory;
import com.example.experienceexchange.util.factory.UserFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService implements IAuthService {

    private final AuthenticationManager manager;
    private final IJwtTokenProvider jwtTokenProvider;
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager manager, IJwtTokenProvider jwtTokenProvider, IUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.manager = manager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public TokenDto authentication(LoginDto loginDto) {
        Authentication authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        JwtUserDetails userDetails = (JwtUserDetails) authentication.getCredentials();
        String token = jwtTokenProvider.createToken(userDetails.getUsername(), userDetails.getRole());
        return TokenDtoFactory.createTokenDto(token, userDetails.getUsername());
    }

    @Transactional
    @Override
    public void registrationUser(AccountDto registrationDto) {
        registration(registrationDto, Role.USER);
    }

    @Transactional
    @Override
    public void registrationAdmin(AccountDto registrationDto) {
        registration(registrationDto, Role.ADMIN);
    }

    private void registration(AccountDto accountDto, Role role) {
        User account = userRepository.findByEmail(accountDto.getEmail());
        if (account != null) {
            throw new EmailNotUniqueException();
        }
        account = UserFactory.createUser(accountDto.getLastName(),
                accountDto.getFirstName(),
                accountDto.getNumberPhone(),
                accountDto.getEmail(),
                passwordEncoder.encode(accountDto.getPassword()),
                Status.ACTIVE,
                role);
        userRepository.save(account);
    }
}
