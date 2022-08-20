package com.example.experienceexchange.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Directions")
public class Direction {
    // TODO: ДОБАВИТЬ ХАРАКТЕРИСТИКИ

    @Id
    @SequenceGenerator(name = "seq_direction", sequenceName = "sequence_id_direction", allocationSize = 1)
    @GeneratedValue(generator = "seq_direction", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "header")
    private String header;

    @OneToMany
    @JoinColumn(name = "direction_id", referencedColumnName = "id")
    private Set<Section> sections = new HashSet<>();

    @ManyToMany(mappedBy = "directions")
    private Set<Product> products = new HashSet<>();

}
