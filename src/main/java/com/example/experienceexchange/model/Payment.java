package com.example.experienceexchange.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Payments")
public class Payment {
    // TODO: МОЖНО СДЕЛАТЬ ИСТОРИЮ ПОКУПОК С ПОМОЩЬЮ ЭТОГО КЛАССА
    // TODO: ЕСЛИ НЕ ОПЛАТИЛ ПРОШЛЫЙ КУРС ТО НЕ МОЖЕШЬ НАЧАТЬ СЛЕДУЮЩИЙ

    @Id
    @SequenceGenerator(name = "seq_pay", sequenceName = "sequence_id_payment", allocationSize = 1)
    @GeneratedValue(generator = "seq_pay", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "date_payment")
    private Date datePayment;

    @Column(name = "is_paid")
    private Boolean isPaid;
}
