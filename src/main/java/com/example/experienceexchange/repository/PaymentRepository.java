package com.example.experienceexchange.repository;

import com.example.experienceexchange.model.Payment;
import com.example.experienceexchange.repository.interfaceRepo.IPaymentRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentRepository extends HibernateAbstractDao<Payment, Long> implements IPaymentRepository {
    //
}
