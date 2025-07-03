package com.example.royaumedekaamelott.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "chevalier")
public class ChevalierEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "titre")
    private String titre;

    @Column(name = "caracteristique_principale")
    private String caracteristiquePrincipale;

    @Column(name = "niveau_bravoure")
    private Integer niveauBravoure;

}
