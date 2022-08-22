package com.example.experienceexchange.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
/*@Entity
@Table( name = "subscriptions")*/
public class Subscription {

    @Id
    @SequenceGenerator(name = "seq_subs", sequenceName = "sequence_id_subscriptions", allocationSize = 1)
    @GeneratedValue(generator = "seq_subs",strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne
    private User user;
    @OneToOne
    private Product product;
}
