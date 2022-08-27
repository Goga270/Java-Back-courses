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
@Table(name = "comments")
public class Comment {
    @Id
    @SequenceGenerator(name = "seq_comments", sequenceName = "sequence_id_comments", allocationSize = 1)
    @GeneratedValue(generator = "seq_comments", strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    private String header;

    private String body;

    @Column(name = "date_created")
    private Date created;

    private Integer rating;
}
