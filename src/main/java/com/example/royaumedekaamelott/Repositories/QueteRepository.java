package com.example.royaumedekaamelott.Repositories;

import com.example.royaumedekaamelott.Entities.QueteEntity;
import com.example.royaumedekaamelott.Enumeration.Difficulte;
import com.example.royaumedekaamelott.Enumeration.StatutParticipation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QueteRepository extends JpaRepository<QueteEntity, Integer> {
    @Query("SELECT DISTINCT q FROM QueteEntity q " +
            "LEFT JOIN ParticipationQueteEntity p ON p.quete = q " +
            "WHERE q.difficulte = :difficulte " +
            "AND (p IS NULL OR p.statutParticipation = :statutEnCours)")
    List<QueteEntity> findAberranteNonCommenceeOuEnCours(
            @Param("difficulte") Difficulte difficulte,
            @Param("statutEnCours") StatutParticipation statutEnCours);
}
