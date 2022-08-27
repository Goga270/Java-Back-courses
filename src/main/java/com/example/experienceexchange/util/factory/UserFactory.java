package com.example.experienceexchange.util.factory;

import com.example.experienceexchange.constant.Role;
import com.example.experienceexchange.constant.Status;
import com.example.experienceexchange.model.User;

import java.util.Date;

public class UserFactory {

    public static User createUser(String lastName,
                                  String firstName,
                                  String patronymic,
                                  String numberPhone,
                                  String email,
                                  String password,
                                  Status status,
                                  Date created,
                                  Date updated,
                                  Role role,
                                  Integer age,
                                  String numberCard) {
        User account = new User();
        account.setLastname(lastName);
        account.setFirstname(firstName);
        account.setPatronymic(patronymic);
        account.setNumberPhone(numberPhone);
        account.setEmail(email);
        account.setPassword(password);
        account.setStatus(status);
        account.setRole(role);
        account.setCreated(created);
        account.setUpdated(updated);
        account.setAge(age);
        account.setNumberCard(numberCard);
        return account;
    }
}
