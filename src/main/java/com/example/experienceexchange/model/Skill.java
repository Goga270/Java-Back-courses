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
@Table(name = "Skills")
public class Skill {
    @Id
    @SequenceGenerator(name = "seq_skills", sequenceName = "sequence_id_skills", allocationSize = 1)
    @GeneratedValue(generator = "seq_skills",strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
        // TODO : competency - подходит для ообзначении профессии ( можно заменить на skill)
    @ManyToMany(mappedBy = "skills")
    private Set<Product> products = new HashSet<>();
}
