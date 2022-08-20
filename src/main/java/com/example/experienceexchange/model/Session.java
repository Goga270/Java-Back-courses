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
@Table(name = "Sessions")
public class Session {

    @Id
    @SequenceGenerator(name = "seq_session", sequenceName = "sequence_id_session", allocationSize = 1)
    @GeneratedValue(generator = "seq_session", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "date_start_lesson")
    private Date startLesson;

    @Column(name = "date_end_lesson")
    private Date endLesson;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @Column(name = "is_current_session")
    private Boolean isCurrentSession;

    // TODO: ДОБАВИТЬ СЮДА ОТСЛЕЖИВАНИЕ ПОСЕЩЯЕМОСТИ?
}
