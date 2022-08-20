package com.example.experienceexchange.repository.interfaceRepo;

import com.example.experienceexchange.model.User;

public interface IUserRepository extends GenericDao<User, Long> {

    User findByEmail(String email);
}
