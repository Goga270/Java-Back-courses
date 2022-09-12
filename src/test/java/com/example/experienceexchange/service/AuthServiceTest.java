package com.example.experienceexchange.service;

import com.example.experienceexchange.constant.Role;
import com.example.experienceexchange.constant.Status;
import com.example.experienceexchange.dto.LoginDto;
import com.example.experienceexchange.dto.TokenDto;
import com.example.experienceexchange.dto.UserDto;
import com.example.experienceexchange.exception.EmailNotUniqueException;
import com.example.experienceexchange.exception.UserNotFoundException;
import com.example.experienceexchange.model.User;
import com.example.experienceexchange.repository.UserRepository;
import com.example.experienceexchange.security.JwtUserDetails;
import com.example.experienceexchange.security.provider.IJwtTokenProvider;
import com.example.experienceexchange.util.date.DateUtil;
import com.example.experienceexchange.util.factory.TokenDtoFactory;
import com.example.experienceexchange.util.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private static final String TOKEN = "token";
    private static final String USERNAME = "username";
    private static final Role USER_ROLE = Role.USER;
    private static final Role ADMIN_ROLE = Role.ADMIN;
    private static final String PASSWORD = "password";
    private static final Long ID = 1L;
    private static final Status ACTIVE_STATUS = Status.ACTIVE;
    private static final Status NOT_ACTIVE_STATUS = Status.NOT_ACTIVE;

    @InjectMocks
    private AuthService authService;
    @Mock
    private IJwtTokenProvider jwtTokenProvider;
    @Mock
    private AuthenticationManager manager;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;
    @Mock
    private TokenDtoFactory tokenDtoFactory;
    @Mock
    private DateUtil dateUtil;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_returnTokenDto_when_authenticationDataCorrect() {
        LoginDto loginDto = mock(LoginDto.class);
        JwtUserDetails userDetails = mock(JwtUserDetails.class);
        Authentication authentication = mock(Authentication.class);
        TokenDto expectedTokenDto = mock(TokenDto.class);
        when(manager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getRole()).thenReturn(USER_ROLE);
        when(userDetails.getUsername()).thenReturn(USERNAME);
        when(jwtTokenProvider.createToken(USERNAME, USER_ROLE)).thenReturn(TOKEN);
        when(tokenDtoFactory.createTokenDto(TOKEN, USERNAME)).thenReturn(expectedTokenDto);

        TokenDto actualTokenDto = authService.authentication(loginDto);

        assertNotNull(actualTokenDto);
        assertEquals(expectedTokenDto, actualTokenDto);
    }

    @Test
    void should_throwException_when_authenticationDataIncorrect() {
        LoginDto loginDto = mock(LoginDto.class);
        when(manager.authenticate(any())).thenThrow(new BadCredentialsException("Неверные учетные данные пользователя"));

        AuthenticationException exception = assertThrows(BadCredentialsException.class, () -> authService.authentication(loginDto));

        assertEquals("Неверные учетные данные пользователя", exception.getMessage());
    }

    @Test
    void should_saveNewUserAccountInDatabase_when_emailUnique() {
        UserDto userDto = mock(UserDto.class);
        User newUser = mock(User.class);
        Date date = Date.from(Instant.now());
        when(userDto.getEmail()).thenReturn(USERNAME);
        when(userDto.getPassword()).thenReturn(PASSWORD);
        when(userRepository.findByEmail(USERNAME)).thenReturn(null);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(PASSWORD);
        when(dateUtil.dateTimeNow()).thenReturn(date);
        when(userMapper.userDtoToUser(userDto)).thenReturn(newUser);

        authService.registrationUser(userDto);

        verify(userDto).setStatus(Status.ACTIVE);
        verify(userDto).setPassword(PASSWORD);
        verify(userDto).setRole(USER_ROLE);
        verify(userRepository).save(newUser);
    }

    @Test
    void should_saveNewAdminAccountInDatabase_when_emailUnique() {
        UserDto userDto = mock(UserDto.class);
        User newUser = mock(User.class);
        Date date = Date.from(Instant.now());
        when(userDto.getEmail()).thenReturn(USERNAME);
        when(userDto.getPassword()).thenReturn(PASSWORD);
        when(userRepository.findByEmail(USERNAME)).thenReturn(null);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(PASSWORD);
        when(dateUtil.dateTimeNow()).thenReturn(date);
        when(userMapper.userDtoToUser(userDto)).thenReturn(newUser);

        authService.registrationAdmin(userDto);

        verify(userDto).setStatus(Status.ACTIVE);
        verify(userDto).setPassword(PASSWORD);
        verify(userDto).setRole(ADMIN_ROLE);
        verify(userRepository).save(newUser);
    }

    @Test
    void should_throwEmailNotUniqueException_when_emailNotUnique() {
        UserDto userDto = mock(UserDto.class);
        User user = mock(User.class);
        when(userDto.getEmail()).thenReturn(USERNAME);
        when(userRepository.findByEmail(USERNAME)).thenReturn(user);

        EmailNotUniqueException exception = assertThrows(EmailNotUniqueException.class, () -> authService.registrationUser(userDto));

        verify(userRepository).findByEmail(USERNAME);
        assertEquals(new EmailNotUniqueException().getMessage(),exception.getMessage());
    }

    @Test
    void should_blockUser_when_enteredIdCorrect() {
        User user = mock(User.class);
        when(userRepository.find(ID)).thenReturn(user);

        authService.blockUser(ID);

        verify(userRepository).update(user);
        verify(user).setStatus(NOT_ACTIVE_STATUS);
    }

    @Test
    void should_throwUserNotFoundException_when_enteredIdIncorrect() {
        when(userRepository.find(ID)).thenReturn(null);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> authService.blockUser(ID));

        assertEquals(String.format("User with id %d not found", ID), exception.getMessage());
    }

    @Test
    void should_unblockUser_when_enteredIdCorrect() {
        User user = mock(User.class);
        when(userRepository.find(ID)).thenReturn(user);

        authService.unblockUser(ID);

        verify(userRepository).update(user);
        verify(user).setStatus(ACTIVE_STATUS);
    }
}