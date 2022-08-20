package com.example.experienceexchange.util.mapper;

import com.example.experienceexchange.dto.AccountDto;
import com.example.experienceexchange.model.Account;
import com.example.experienceexchange.model.User;
import com.example.experienceexchange.security.JwtUserDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    AccountDto UserToAccountDto(User user);

    @Mapping(target = "validated", expression = "java(String.valueOf(user.getStatus()).equals(\"ACTIVE\"))")
    JwtUserDetails UserToUserDetails(User user);

    @Mapping(target = "status", defaultValue = "ACTIVE")
    User AccountDtoToUser(AccountDto accountDto);
}
