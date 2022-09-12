package com.example.experienceexchange.service;

import com.example.experienceexchange.constant.Role;
import com.example.experienceexchange.constant.Status;
import com.example.experienceexchange.dto.LoginDto;
import com.example.experienceexchange.dto.TokenDto;
import com.example.experienceexchange.dto.UserDto;
import com.example.experienceexchange.exception.EmailNotUniqueException;
import com.example.experienceexchange.exception.UserNotFoundException;
import com.example.experienceexchange.model.User;
import com.example.experienceexchange.repository.interfaceRepo.IUserRepository;
import com.example.experienceexchange.security.JwtUserDetails;
import com.example.experienceexchange.security.provider.IJwtTokenProvider;
import com.example.experienceexchange.service.interfaceService.IAuthService;
import com.example.experienceexchange.util.date.DateUtil;
import com.example.experienceexchange.util.factory.TokenDtoFactory;
import com.example.experienceexchange.util.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AuthService implements IAuthService {

    private final AuthenticationManager manager;
    private final IJwtTokenProvider jwtTokenProvider;
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final TokenDtoFactory tokenDtoFactory;
    private final DateUtil dateUtil;

    public AuthService(AuthenticationManager manager,
                       IJwtTokenProvider jwtTokenProvider,
                       IUserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       UserMapper userMapper,
                       TokenDtoFactory tokenDtoFactory,
                       DateUtil dateUtil) {
        this.manager = manager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.tokenDtoFactory = tokenDtoFactory;
        this.dateUtil = dateUtil;
    }

    @Transactional
    @Override
    public TokenDto authentication(LoginDto loginDto) {
        log.debug("Authentication user");
        Authentication authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();

        String token = jwtTokenProvider.createToken(userDetails.getUsername(), userDetails.getRole());

        log.debug("User with id={} is authenticated", userDetails.getId());
        return tokenDtoFactory.createTokenDto(token, userDetails.getUsername());
    }

    @Transactional
    @Override
    public void registrationUser(UserDto registrationDto) {
        registrationDto.setRole(Role.USER);
        registration(registrationDto);
    }

    @Transactional
    @Override
    public void registrationAdmin(UserDto registrationDto) {
        registrationDto.setRole(Role.ADMIN);
        registration(registrationDto);
    }

    @Transactional
    @Override
    public void blockUser(Long id) {
        log.debug("Block user with id={}", id);
        changeStatusUser(id, Status.NOT_ACTIVE);
        log.debug("Blocked user {}", id);
    }

    @Transactional
    @Override
    public void unblockUser(Long id) {
        log.debug("Unblock user with id={}", id);
        changeStatusUser(id, Status.ACTIVE);
        log.debug("Unblocked user {}", id);
    }

    private void registration(UserDto userDto) {
        log.debug("Registration new user");
        User account = userRepository.findByEmail(userDto.getEmail());
        if (account != null) {
            log.warn("Email is not unique, new user not registered");
            throw new EmailNotUniqueException();
        }
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userDto.setStatus(Status.ACTIVE);
        userDto.setCreated(dateUtil.dateTimeNow());
        userDto.setUpdated(dateUtil.dateTimeNow());
        User newUser = userMapper.userDtoToUser(userDto);
        userRepository.save(newUser);
        log.debug("Registered new user {} with role={}", userDto.getRole(), newUser.getId());
    }

    private void changeStatusUser(Long id, Status newStatus) {
        User user = userRepository.find(id);
        if (user != null) {
            user.setStatus(newStatus);
            userRepository.update(user);
        } else {
            log.warn("User {} is not found", id);
            throw new UserNotFoundException(id);
        }
    }
}
