package com.example.experienceexchange.model;

import com.example.experienceexchange.constant.TypeLesson;
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
@Table(name = "single_lessons")
public class LessonSingle extends Lesson {

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

    @Column(name = "type_lesson")
    @Enumerated(EnumType.STRING)
    private TypeLesson typeLesson;

    @ManyToMany(mappedBy = "lessonSubscriptions")
    private Set<User> usersInLesson = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.REFRESH})
    @JoinTable(
            name = "lesson_section",
            joinColumns = {@JoinColumn(name = "lesson_id")},
            inverseJoinColumns = {@JoinColumn(name = "section_id")}
    )
    private Set<Section> sections = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.REFRESH})
    @JoinTable(
            name = "lesson_direction",
            joinColumns = {@JoinColumn(name = "lesson_id")},
            inverseJoinColumns = {@JoinColumn(name = "direction_id")}
    )
    private Set<Direction> directions = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.REFRESH})
    @JoinTable(
            name = "lesson_skill",
            joinColumns = {@JoinColumn(name = "lesson_id")},
            inverseJoinColumns = {@JoinColumn(name = "skill_id")}
    )
    private Set<Skill> skills = new HashSet<>();

    @OneToMany(mappedBy = "lesson", cascade = {
            CascadeType.PERSIST,
            CascadeType.DETACH,
            CascadeType.REMOVE}, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    public Boolean isAvailableForSubscription(Date nowDate) {
        return currentNumberUsers.compareTo(maxNumberUsers) < 0
                && getStartLesson().after(nowDate)
                && getEndLesson().after(nowDate);
    }

    public Boolean isSatisfactoryPrice(BigDecimal enteredPrice) {
        return price.compareTo(enteredPrice) <= 0;
    }

    public synchronized void increaseNumberSubscriptions() {
        currentNumberUsers++;
    }
}
