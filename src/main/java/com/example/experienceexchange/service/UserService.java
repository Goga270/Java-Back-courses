package com.example.experienceexchange.service;

import com.example.experienceexchange.dto.UserDto;
import com.example.experienceexchange.dto.LessonDto;
import com.example.experienceexchange.dto.NewEmailDto;
import com.example.experienceexchange.dto.NewPasswordDto;
import com.example.experienceexchange.exception.EmailNotUniqueException;
import com.example.experienceexchange.exception.PasswordsNotMatchException;
import com.example.experienceexchange.exception.UserNotFoundException;
import com.example.experienceexchange.model.Lesson;
import com.example.experienceexchange.model.User;
import com.example.experienceexchange.repository.interfaceRepo.IUserRepository;
import com.example.experienceexchange.security.JwtUserDetails;
import com.example.experienceexchange.service.interfaceService.IUserService;
import com.example.experienceexchange.util.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class UserService implements IUserService {

    private final IUserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(IUserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto getAccount(JwtUserDetails userDetails) {
        Long userId = userDetails.getId();
        User user = getUserById(userId);
        return userMapper.userToUserDto(user);
    }

    @Transactional
    @Override
    public UserDto editAccount(JwtUserDetails userDetails, UserDto userDto) {
        Long userId = userDetails.getId();
        User oldUser = getUserById(userId);
        User newUser = userMapper.updateUser(oldUser, userDto);
        userRepository.update(newUser);
        return userMapper.userToUserDto(newUser);
    }

    @Transactional
    @Override
    public void changePassword(JwtUserDetails userDetails, NewPasswordDto passwordDto) {
        if (passwordDto.getNewPassword().equals(passwordDto.getNewPasswordSecond())) {
            User user = userRepository.find(userDetails.getId());
            String encodeNewPassword = passwordEncoder.encode(passwordDto.getNewPassword());
            user.setPassword(encodeNewPassword);
            userRepository.update(user);
        } else {
            throw new PasswordsNotMatchException();
        }
    }

    @Transactional
    @Override
    public Set<LessonDto> getSchedule(JwtUserDetails userDetails) {
        Long userId = userDetails.getId();
        User user = getUserById(userId);
//        Set<Lesson> subscriptions = user.getLessonSubscriptions/**/();
        return null;
    }

    @Transactional
    @Override
    public void changeEmail(JwtUserDetails jwtUserDetails, NewEmailDto newEmailDto) {
        User user = userRepository.findByEmail(newEmailDto.getNewEmail());
        if (user != null) {
            throw new EmailNotUniqueException();
        }
        User userForUpdate = userRepository.find(jwtUserDetails.getId());
        userForUpdate.setEmail(newEmailDto.getNewEmail());
        userRepository.update(userForUpdate);
    }

    private User getUserById(Long id) {
        User user = userRepository.find(id);
        if (user == null) {
            throw new UserNotFoundException(id);
        }
        return user;
    }
}
