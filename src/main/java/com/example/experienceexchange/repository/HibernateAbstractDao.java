package com.example.experienceexchange.repository;


import com.example.experienceexchange.repository.interfaceRepo.GenericDao;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import java.io.Serializable;
import java.util.List;

public abstract class HibernateAbstractDao<T, PK extends Serializable> implements GenericDao<T, PK> {

    protected Class<T> type;

    @PersistenceContext
    protected EntityManager entityManager;

    @Override
    public T save(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public T update(T entity) {
        return entityManager.merge(entity);
    }

    @Override
    public void deleteById(PK id) {
        T entity = find(id);
        if (entity != null) {
            try {
                entityManager.remove(entity);
            } catch (Exception e) {
                System.out.println(e.getMessage() + e.getClass().getName());
            }
        } else {
            throw new EntityExistsException();
        }
    }

    @Override
    public void delete(T entity) {
        if (entity != null) {
            try {
                entityManager.remove(entity);
            } catch (Exception e) {
                System.out.println(e.getMessage() + e.getClass().getName());
            }
        } else {
            throw new EntityExistsException();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        return entityManager.createQuery("from " + type.getName()).getResultList();
    }

    @Override
    public T find(PK id) {
        return entityManager.find(type, id);
    }

    @Override
    public boolean exists(PK id) {
        return find(id) != null;
    }

    @Override
    public long count() {
        return findAll().size();
    }

    public final void setClass(Class<T> type) {
        this.type = type;
    }

    protected final Class<T> getClassEntity() {
        return type;
    }

    protected final CriteriaBuilder getCriteriaBuilder() {
        return entityManager.getCriteriaBuilder();
    }
}

