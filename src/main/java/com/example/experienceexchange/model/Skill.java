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
@Table(name = "skills")
// TODO : ЮЗЕРЫ ТОЖЕ МОГУТ ИХ СОЗДАВАТЬ!
public class Skill {

    @Id
    @SequenceGenerator(name = "seq_skills", sequenceName = "sequence_id_skills", allocationSize = 1)
    @GeneratedValue(generator = "seq_skills",strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "skills", cascade = CascadeType.ALL)
    private Set<Course> courses = new HashSet<>();

    @ManyToMany(mappedBy = "skills", cascade = CascadeType.ALL)
    private Set<LessonSingle> lessonSingles = new HashSet<>();
}
