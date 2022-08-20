package com.example.experienceexchange.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Users")
@Setter
@Getter
@NoArgsConstructor
public class User extends Account {

    // TODO: УДАЛЕНИЕ ПРИ MANY TO MANY
    @Id
    @SequenceGenerator(name = "seq_user", sequenceName = "sequence_id_user", allocationSize = 1)
    @GeneratedValue(generator = "seq_user", strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToMany(mappedBy = "author")
    private Set<Product> myProducts = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_lesson",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "lesson_id")}
    )
    private Set<Lesson> lessons = new HashSet<>();
}
/*
* */

