package com.example.experienceexchange.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
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
    @Column(name = "skill_level")
    private Integer skillLevel;

    @Column(name = "purpose_of_knowledge")
    private Integer purposeKnowledge;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @Column(name = "max_number_users")
    private Integer maxNumberUsers;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "current_number_users")
    private volatile Integer currentNumberUsers = 0;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "product_section",
            joinColumns = {@JoinColumn(name = "product_id")},
            inverseJoinColumns = {@JoinColumn(name = "section_id")}
    )
    private Set<Section> sections = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "product_direction",
            joinColumns = {@JoinColumn(name = "product_id")},
            inverseJoinColumns = {@JoinColumn(name = "direction_id")}
    )
    private Set<Direction> directions = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "product_skill",
            joinColumns = {@JoinColumn(name = "product_id")},
            inverseJoinColumns = {@JoinColumn(name = "skill_id")}
    )
    private Set<Skill> skills = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "product_user",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "product_id")}
    )
    private Set<User> usersCourse= new HashSet<>();

    @OneToMany
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Set<Comment> comments = new HashSet<>();
}
