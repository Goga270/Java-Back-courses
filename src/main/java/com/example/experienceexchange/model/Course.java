package com.example.experienceexchange.model;


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
@Table(name = "courses")
public class Course extends Product {

    @OneToMany
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Set<Lesson> lessons = new HashSet<>();

    @Column(name = "date_start_course")
    private Date dateStart;

    @Column(name = "date_end_course")
    private Date dateEnd;

    @Column(name = "current_number_users")
    private volatile Integer currentNumberUsers = 0;

/*    @Column(name = "course_duration")
    private Integer courseDuration;*/

    @ManyToMany(mappedBy = "courseSubscriptions")
    private Set<User> usersInCourse = new HashSet<>();

    public void addComment(Comment comment) {
        getComments().add(comment);
    }
}
