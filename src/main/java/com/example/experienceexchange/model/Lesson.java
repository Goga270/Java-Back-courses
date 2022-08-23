package com.example.experienceexchange.model;

import com.example.experienceexchange.constant.TypeLesson;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

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
    @Column(name = "date_start_lesson")
    private Date startLesson;
    @Column(name = "date_end_lesson")
    private Date endLesson;
}
