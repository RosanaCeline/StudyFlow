package com.studyflow.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subject")
@Getter
@Setter
@RequiredArgsConstructor
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = true, length = 250)
    private String  description;

    @Column(nullable = false)
    private String color;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Situation situation;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    private List<Task> task = new ArrayList<>();
}
