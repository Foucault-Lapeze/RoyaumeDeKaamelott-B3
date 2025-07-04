package com.example.royaumedekaamelott.Repositories;

import com.example.royaumedekaamelott.Entities.ParticipationQueteEntity;
import com.example.royaumedekaamelott.Entities.QueteEntity;
import com.example.royaumedekaamelott.Enumeration.Difficulte;
import com.example.royaumedekaamelott.Enumeration.StatutParticipation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.util.List;

public interface QueteRepository extends JpaRepository<QueteEntity, Integer> {
    @Query("SELECT DISTINCT q FROM QueteEntity q " +
            "LEFT JOIN ParticipationQueteEntity p ON p.quete = q " +
            "WHERE q.difficulte = :difficulte " +
            "AND (p IS NULL OR p.statutParticipation = :statutEnCours)")
    List<QueteEntity> findAberranteNonCommenceeOuEnCours(
            @Param("difficulte") Difficulte difficulte,
            @Param("statutEnCours") StatutParticipation statutEnCours);

    @Query("SELECT q FROM QueteEntity q " +
            "LEFT JOIN ParticipationQueteEntity p ON p.quete = q " +
            "GROUP BY q.id " +
            "HAVING COUNT(p.chevalier) < :minChevaliers")
    List<QueteEntity> findQuetesAvecMoinsDeChevaliers(@Param("minChevaliers") long minChevaliers);

    //exercice 10
    @Query("SELECT q FROM QueteEntity q " +
            "ORDER BY DATEDIFF(q.dateEcheance, q.dateAssignation) DESC")
    List<QueteEntity> findAllOrderByDureeDesc(PageRequest pageRequest);

//exercice 11
    @Query("SELECT q FROM QueteEntity q " +
            "WHERE q.dateAssignation <= :dateFin " +
            "AND q.dateEcheance >= :dateDebut")
    List<QueteEntity> findQuetesParChevauchement(
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin
    );

    //exercice 13
    int countByDateAssignationBetween(LocalDate debut, LocalDate fin);

}
