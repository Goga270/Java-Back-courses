package com.example.experienceexchange.mapper;

import com.example.experienceexchange.constant.Status;
import com.example.experienceexchange.model.User;
import com.example.experienceexchange.security.JwtUserDetails;
import com.example.experienceexchange.util.date.DateUtil;
import com.example.experienceexchange.util.mapper.UserMapper;
import com.example.experienceexchange.util.mapper.UserMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IUserMapperTest {

    private final static String EMAIL = "email@mail.ru";
    private final static Boolean VALID = true;
    private final static Status STATUS_ACTIVE = Status.ACTIVE;


    @InjectMocks
    private UserMapperImpl userMapper;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void should_returnUserDetails_when_takeUser() {
        User account = mock(User.class);
        when(account.getEmail()).thenReturn(EMAIL);
        when(account.getStatus()).thenReturn(STATUS_ACTIVE);

        JwtUserDetails jwtUserDetails = userMapper.UserToUserDetails(account);

        assertNotNull(jwtUserDetails);
        assertEquals(EMAIL, jwtUserDetails.getUsername());
        assertEquals(VALID, jwtUserDetails.getValidated());
    }
}
