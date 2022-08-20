package com.example.experienceexchange.repository.interfaceRepo;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface GenericDao<T, PK extends Serializable> {

    T save(T entity);

    T update(T entity);

    void delete(T entity);

    void deleteById(PK id);

    List<T> findAll();

    T find(PK id);

    boolean exists(PK id);

    long count();
}
