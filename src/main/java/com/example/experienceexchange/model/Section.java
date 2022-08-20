package com.example.experienceexchange.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Sections")
public class Section {

    @Id
    @SequenceGenerator(name = "seq_sections", sequenceName = "sequence_id_sections", allocationSize = 1)
    @GeneratedValue(generator = "seq_sections", strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToMany(mappedBy = "sections")
    private Set<Product> products = new HashSet<>();
}
