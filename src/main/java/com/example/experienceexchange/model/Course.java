package com.example.experienceexchange.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @SequenceGenerator(name = "seq_courses", sequenceName = "sequence_id_courses", allocationSize = 1)
    @GeneratedValue(generator = "seq_courses", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "skill_level")
    private Integer masteryLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    @Column(name = "max_number_users")
    private Integer maxNumberUsers;

    @Column(name = "current_number_users")
    private volatile Integer currentNumberUsers;

    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "date_start_course")
    private Date dateStart;

    @Column(name = "date_end_course")
    private Date dateEnd;

    @ManyToMany(cascade = {CascadeType.REFRESH})
    @JoinTable(
            name = "course_section",
            joinColumns = {@JoinColumn(name = "course_id")},
            inverseJoinColumns = {@JoinColumn(name = "section_id")}
    )
    private Set<Section> sections = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.REFRESH})
    @JoinTable(
            name = "course_direction",
            joinColumns = {@JoinColumn(name = "course_id")},
            inverseJoinColumns = {@JoinColumn(name = "direction_id")}
    )
    private Set<Direction> directions = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.REFRESH})
    @JoinTable(
            name = "course_skill",
            joinColumns = {@JoinColumn(name = "course_id")},
            inverseJoinColumns = {@JoinColumn(name = "skill_id")}
    )
    private Set<Skill> skills = new HashSet<>();


    @OneToMany(mappedBy = "course", cascade = {
            CascadeType.PERSIST,
            CascadeType.DETACH,
            CascadeType.REMOVE}, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "course", cascade = {
            CascadeType.PERSIST,
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REMOVE}, orphanRemoval = true)
    private Set<LessonOnCourse> lessons = new HashSet<>();

    @ManyToMany(mappedBy = "courseSubscriptions")
    private Set<User> usersInCourse = new HashSet<>();

    public void addLesson(LessonOnCourse lessonOnCourse) {
        lessons.add(lessonOnCourse);
    }

    public Boolean isAvailableForSubscription(Date nowDate) {
        return currentNumberUsers.compareTo(maxNumberUsers) < 0
                && dateStart.after(nowDate)
                && dateEnd.after(nowDate);
    }

    public Boolean isSatisfactoryPrice(BigDecimal enteredPrice) {
        return price.compareTo(enteredPrice) <= 0;
    }

    public synchronized void increaseNumberSubscriptions() {
        currentNumberUsers++;
    }
}
