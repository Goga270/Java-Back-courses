package com.example.experienceexchange.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Lesson {

    @Id
    @SequenceGenerator(name = "seq_lessons", sequenceName = "sequence_id_lessons", allocationSize = 1)
    @GeneratedValue(generator = "seq_lessons", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "link_homework")
    private String homeworkLink;

    @Column(name = "link_video")
    private String linkVideo;

    @Column(name = "time_start_lesson")
    private Date startLesson;

    @Column(name = "time_end_lesson")
    private Date endLesson;
}
