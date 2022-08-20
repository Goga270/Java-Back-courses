package com.example.experienceexchange.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "Comments")
// TODO : связь через доп таблицу ? наверно нет
// TODO : У КУРСОВ И ЗАНЯТИЙ НЕ ДОДЖНО БЫТЬ ОДИНАКОВЫХ АЙДИ
public class Comment {

    @Id
    @SequenceGenerator(name = "seq_comment", sequenceName = "sequence_id_comment", allocationSize = 1)
    @GeneratedValue(generator = "seq_comment", strategy = GenerationType.SEQUENCE)
    private Long id;
    private String header;
    private String body;
    @Column(name = "date_created")
    private Date created;
    @Column(name = "date_updated")
    private Date updated;
    private Integer rating;
}
