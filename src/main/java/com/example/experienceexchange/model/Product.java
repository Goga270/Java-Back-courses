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
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Product {

    @Id
    @SequenceGenerator(name = "seq_products", sequenceName = "sequence_id_products", allocationSize = 1)
    @GeneratedValue(generator = "seq_products", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    // TODO : ОГРАНИЧИТЬ В ДТО
    // TODO : SESSION - В ЗНАЧЕНИИ ПРОХОЖДЕНИЯ ЗАНЯТИЯ
    @Column(name = "skill_level")
    private Integer skillLevel;

    @Column(name = "purpose_of_knowledge")
    private Integer purposeKnowledge;

    @Column(name = "course_duration")
    private Integer courseDuration;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "product_section",
            joinColumns = {@JoinColumn(name = "product_id")},
            inverseJoinColumns = {@JoinColumn(name = "section_id")}
    )
    private Set<Section> sections = new HashSet<>();


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "product_skill",
            joinColumns = {@JoinColumn(name = "product_id")},
            inverseJoinColumns = {@JoinColumn(name = "skill_id")}
    )
    private Set<Skill> skills = new HashSet<>();

    @OneToMany
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Set<Comment> comments = new HashSet<>();
}
