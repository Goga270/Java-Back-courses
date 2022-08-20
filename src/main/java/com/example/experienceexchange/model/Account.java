package com.example.experienceexchange.model;

import com.example.experienceexchange.constant.Role;
import com.example.experienceexchange.constant.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class Account {

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "numberPhone")
    private String numberPhone;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "date_created")
    private Date created;

    @Column(name = "date_updated")
    private Date updated;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Column(name = "age")
    private Integer age;

    @JoinColumn(name = "number_card")
    private String numberCard;
}