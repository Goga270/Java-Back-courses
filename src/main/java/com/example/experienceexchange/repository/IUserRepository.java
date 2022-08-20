package com.example.experienceexchange.repository;

import com.example.experienceexchange.model.Account;
import com.example.experienceexchange.model.User;

public interface IUserRepository extends GenericDao<User, Long> {

    User findByEmail(String email);
}
