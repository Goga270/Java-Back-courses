package com.example.experienceexchange.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends Account {

    @Id
    @SequenceGenerator(name = "seq_user", sequenceName = "sequence_id_users", allocationSize = 1)
    @GeneratedValue(generator = "seq_user", strategy = GenerationType.SEQUENCE)
    private Long id;


    @OneToMany(mappedBy = "author")
    private Set<LessonSingle> createdLessons = new HashSet<>();

    @OneToMany(mappedBy = "author")
    private Set<Course> createdCourses = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_lesson",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "lesson_id")}
    )
    private Set<LessonSingle> lessonSubscriptions = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_course",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "course_id")}
    )
    private Set<Course> courseSubscriptions = new HashSet<>();

    @OneToMany(mappedBy = "costumer")
    private List<Payment> myPayments = new LinkedList<>();

    public void addLesson(LessonSingle lesson) {
        lessonSubscriptions.add(lesson);
    }

    public void addPayment(Payment payment) {
        myPayments.add(payment);
    }

    public void addCourse(Course course) {
        courseSubscriptions.add(course);
    }
}


