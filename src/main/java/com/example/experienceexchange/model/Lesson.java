package com.example.experienceexchange.model;

import com.example.experienceexchange.constant.TypeLesson;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
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
    @Column(name = "access_duration")
    private Integer accessDuration;
    @Column(name = "time_start_lesson")
    private Date startLesson;
    @Column(name = "time_end_lesson")
    private Date endLesson;

    @ManyToMany(mappedBy = "lessonSubscriptions")
    private Set<User> usersInLesson = new HashSet<>();
}
