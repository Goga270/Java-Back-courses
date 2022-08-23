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

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "number_phone")
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

    @Column(name = "number_card")
    private String numberCard;
}