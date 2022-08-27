package com.example.experienceexchange.repository;

import com.example.experienceexchange.model.User;
import com.example.experienceexchange.repository.interfaceRepo.IUserRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
public class UserRepository extends HibernateAbstractDao<User, Long> implements IUserRepository {

    @Override
    public User findByEmail(String email) {
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(getClassEntity());
        Root<User> root = cq.from(getClassEntity());

        cq.select(root).where(cb.equal(root.get("email"), email));
        try {
            User user = entityManager.createQuery(cq).getSingleResult();
            return user;
        } catch (NoResultException e) {
            return null;
        }
    }
}
