package com.example.experienceexchange.repository;

import com.example.experienceexchange.model.Account;
import com.example.experienceexchange.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
public class UserRepository extends HibernateAbstractDao<User, Long> implements IUserRepository {

    @Override
    public User findByEmail(String email) {
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = cb.createQuery(getClassEntity());
        Root<User> root = criteriaQuery.from(getClassEntity());

        criteriaQuery.select(root).where(cb.equal(root.get("email"), email));

        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }
}