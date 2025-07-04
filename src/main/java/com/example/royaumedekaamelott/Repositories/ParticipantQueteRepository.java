package com.example.royaumedekaamelott.Repositories;

import com.example.royaumedekaamelott.Entities.ParticipationQueteEntity;
import com.example.royaumedekaamelott.Entities.ParticipationQueteId;
import com.example.royaumedekaamelott.Enumeration.StatutParticipation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ParticipantQueteRepository extends JpaRepository<ParticipationQueteEntity, ParticipationQueteId> {
    List<ParticipationQueteEntity> findByQuete_Id(Integer queteId);
    List<ParticipationQueteEntity> findByChevalier_IdAndStatutParticipation(Integer chevalierId, StatutParticipation statutParticipation);

    @Transactional
    @Modifying
    void deleteByChevalier_IdAndQuete_Id(Integer chevalierId, Integer queteId);

    List<ParticipationQueteEntity> findByChevalier_Id(Integer chevalierId);

    //exerecice 13
    //exo 13

    @Query("SELECT COUNT(p) FROM ParticipationQueteEntity p WHERE p.statutParticipation = :statut AND p.quete.dateAssignation BETWEEN :debut AND :fin")
    int countByStatutParticipationAndDateAssignationBetween(
            @Param("statut") StatutParticipation statut,
            @Param("debut") LocalDate debut,
            @Param("fin") LocalDate fin
    );

    @Query("SELECT DISTINCT p.chevalier.id FROM ParticipationQueteEntity p WHERE p.quete.dateAssignation BETWEEN :start AND :end")
    List<Integer> findDistinctChevalierIdsByDateAssignationBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);

    //exercice 13
    @Query("SELECT p FROM ParticipationQueteEntity p WHERE p.statutParticipation = :statut AND p.quete.dateAssignation BETWEEN :debut AND :fin")
    List<ParticipationQueteEntity> findByStatutParticipationAndDateAssignationBetween(
            @Param("statut") StatutParticipation statut,
            @Param("debut") LocalDate debut,
            @Param("fin") LocalDate fin
    );
}

