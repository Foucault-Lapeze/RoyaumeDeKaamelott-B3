package com.example.royaumedekaamelott.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticipantQueteDto {
    private Integer chevalierId;

    private String nom;

    private String titre;

    private String caracteristiquePrincipale;

    private int niveauBravoure;

    private String role;

    private String statutParticipation;

    private String commentaire;
}
