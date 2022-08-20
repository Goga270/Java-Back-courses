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
@Table(name = "Courses")
public class Course extends Product {


    @OneToMany
    @JoinColumn(name = "courses_id", referencedColumnName = "id")
    private Set<Lesson> lessons = new HashSet<>();

    @Column(name = "date_start_course")
    private Date courseStart;

    @Column(name = "date_end_course")
    private Date courseEnd;

    @Column(name ="duration")
    private Integer duration;
}
