package com.example.experienceexchange.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends Account {

    // TODO: УДАЛЕНИЕ ПРИ MANY TO MANY
    @Id
    @SequenceGenerator(name = "seq_user", sequenceName = "sequence_id_users", allocationSize = 1)
    @GeneratedValue(generator = "seq_user", strategy = GenerationType.SEQUENCE)
    private Long id;


    @OneToMany(mappedBy = "author")
    private Set<LessonSingle> createdLessons = new HashSet<>();

    @OneToMany(mappedBy = "author")
    private Set<Course> createdCourses = new HashSet<>();
    // TODO : РЕШИТЬ С ОПЛАТОЙ

    @ManyToMany
    @JoinTable(
            name = "user_lesson",
            joinColumns = {@JoinColumn(name = "lesson_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<LessonSingle> lessonSubscriptions = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_course",
            joinColumns = {@JoinColumn(name = "course_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<Course> courseSubscriptions = new HashSet<>();
    // TODO: ДЛЯ ОПЛАТЫ ПРОСТО СОЗДАВАТЬ JSON PAYMENT ОТДАВАТЬ ДАННЫЕ КОГДА КТО ТО ВЫЗОВЕТ ЭТОТ СКРИПТ И ВСЕ)
}


