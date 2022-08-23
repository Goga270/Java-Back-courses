package com.example.experienceexchange.util.mapper;

import com.example.experienceexchange.constant.Status;
import com.example.experienceexchange.dto.AccountDto;
import com.example.experienceexchange.model.Account;
import com.example.experienceexchange.model.User;
import com.example.experienceexchange.security.JwtUserDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    public abstract AccountDto UserToAccountDto(User user);

    @Mapping(target = "validated", expression = "java(String.valueOf(user.getStatus()).equals(\"ACTIVE\"))")
    public abstract JwtUserDetails UserToUserDetails(User user);

    @Mapping(target = "status", defaultValue = "ACTIVE")
    @Mapping(target = "role", ignore = true)
    public abstract User AccountDtoToUser(AccountDto accountDto);

    public abstract AccountDto userToAccountDto(User user);

    public User updateUser(User oldUser, AccountDto accountDto) {
        oldUser.setLastName(accountDto.getLastName());
        oldUser.setFirstName(accountDto.getFirstName());
        oldUser.setPatronymic(accountDto.getPatronymic());
        oldUser.setNumberPhone(accountDto.getNumberPhone());
        oldUser.setUpdated(new Date());
        oldUser.setAge(accountDto.getAge());
        oldUser.setNumberCard(accountDto.getNumberCard());
        return oldUser;
    }
}
