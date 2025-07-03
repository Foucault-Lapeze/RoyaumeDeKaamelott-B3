package com.example.royaumedekaamelott.Entities;
import com.example.royaumedekaamelott.Entities.ChevalierEntity;
import com.example.royaumedekaamelott.Entities.QueteEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Table;

@Entity
@IdClass(ParticipationQueteId.class)
@Getter
@Setter
@Table(name = "participation_quete")
public class ParticipationQueteEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "id_chevalier")
    private ChevalierEntity chevalier;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_quete")
    private QueteEntity quete;

    @Column(name = "role")
    private String role;

    @Column(name = "statut_participation")
    private String statutParticipation;

    @Column(name = "commentaire_roi")
    private String commentaireRoi;
}
