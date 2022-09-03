package com.example.experienceexchange.repository;

import com.example.experienceexchange.model.Payment;
import com.example.experienceexchange.repository.interfaceRepo.IPaymentRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class PaymentRepository extends HibernateAbstractDao<Payment, Long> implements IPaymentRepository {

    @Override
    public List<Payment> findAllPaymentByUserId(Long userId) {
        CriteriaBuilder cb = getCriteriaBuilder();
        CriteriaQuery<Payment> cq = cb.createQuery(getClassEntity());
        Root<Payment> root = cq.from(getClassEntity());

        cq.select(root).where(cb.equal(root.get("costumer"), userId));

        return entityManager.createQuery(cq).getResultList();
    }
}
