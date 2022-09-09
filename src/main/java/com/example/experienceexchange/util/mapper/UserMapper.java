package com.example.experienceexchange.util.mapper;

import com.example.experienceexchange.dto.UserDto;
import com.example.experienceexchange.model.User;
import com.example.experienceexchange.security.JwtUserDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.util.Date;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Mapping(target = "validated", expression = "java(String.valueOf(user.getStatus()).equals(\"ACTIVE\"))")
    public abstract JwtUserDetails UserToUserDetails(User user);

    public abstract User userDtoToUser(UserDto userDto);

    public abstract UserDto userToUserDto(User user);

    public User updateUser(User oldUser, UserDto userDto) {
        oldUser.setLastname(userDto.getLastname());
        oldUser.setFirstname(userDto.getFirstname());
        oldUser.setPatronymic(userDto.getPatronymic());
        oldUser.setNumberPhone(userDto.getNumberPhone());
        oldUser.setUpdated(Date.from(Instant.now()));
        oldUser.setAge(userDto.getAge());
        oldUser.setNumberCard(userDto.getNumberCard());
        return oldUser;
    }
}
