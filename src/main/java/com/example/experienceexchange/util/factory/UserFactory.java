package com.example.experienceexchange.util.factory;

import com.example.experienceexchange.constant.Role;
import com.example.experienceexchange.constant.Status;
import com.example.experienceexchange.model.User;

import java.util.Date;

public class UserFactory {

    public static User createUser(String lastName, String firstName, String numberPhone, String email, String password, Status status, Role role) {
        User account = new User();
        account.setLastName(lastName);
        account.setFirstName(firstName);
        account.setNumberPhone(numberPhone);
        account.setEmail(email);
        account.setPassword(password);
        account.setStatus(status);
        account.setRole(role);
        account.setCreated(new Date());
        account.setUpdated(account.getCreated());
        return account;
    }
}
