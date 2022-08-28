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
@Table(name = "directions")
public class Direction {

    @Id
    @SequenceGenerator(name = "seq_directions", sequenceName = "sequence_id_directions", allocationSize = 1)
    @GeneratedValue(generator = "seq_directions", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "header")
    private String header;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "direction_id", referencedColumnName = "id")
    private Set<Section> sections = new HashSet<>();

    @ManyToMany(mappedBy = "directions", cascade = CascadeType.ALL)
    private Set<Course> courses = new HashSet<>();

    @ManyToMany(mappedBy = "directions", cascade = CascadeType.ALL)
    private Set<LessonSingle> lessons = new HashSet<>();

}
