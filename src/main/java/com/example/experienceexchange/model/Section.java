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
@Table(name = "sections")
public class Section {

    @Id
    @SequenceGenerator(name = "seq_sections", sequenceName = "sequence_id_sections", allocationSize = 1)
    @GeneratedValue(generator = "seq_sections", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "direction_id")
    private Direction direction;

    @ManyToMany(mappedBy = "sections", cascade = CascadeType.ALL)
    private Set<Course> courses = new HashSet<>();

    @ManyToMany(mappedBy = "sections", cascade = CascadeType.ALL)
    private Set<LessonSingle> lessons = new HashSet<>();
}
