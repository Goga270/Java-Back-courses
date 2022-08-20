package com.example.experienceexchange.model;

import com.example.experienceexchange.constant.TypeLesson;
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
@Table(name = "Lessons")
public class Lesson extends Product {
    // TODO: ЦЕНА


    @Column(name = "type_lesson")
    @Enumerated(EnumType.STRING)
    private TypeLesson typeLesson;
    @Column(name = "link_homework")
    private String homeworkLink;
    @Column(name = "link_video")
    private String linkVideo;
    @OneToOne
    @JoinColumn(name = "current_session_id")
    private Session session;
    // TODO : где хранить данные для дз где хранить возможную ссылку для видеоазписи
    @OneToMany(mappedBy = "lesson")
    private Set<Session> sessions = new HashSet<>();
}
