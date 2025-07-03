package com.example.royaumedekaamelott.Entities;

import com.example.royaumedekaamelott.Enumeration.Difficulte;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "quete")
public class QueteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nom_quete")
    private String nomQuete;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulte")
    private Difficulte difficulte;

    @Column(name = "date_assignation")
    private LocalDate dateAssignation;

    @Column(name = "date_echeance")
    private LocalDate dateEcheance;
}
