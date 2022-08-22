package com.example.experienceexchange.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends Account {

    // TODO: УДАЛЕНИЕ ПРИ MANY TO MANY
    @Id
    @SequenceGenerator(name = "seq_user", sequenceName = "sequence_id_users", allocationSize = 1)
    @GeneratedValue(generator = "seq_user", strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToMany(mappedBy = "author")
    private Set<Product> myProducts = new HashSet<>();
    // TODO : РЕШИТЬ С ОПЛАТОЙ
}


