package com.example.royaumedekaamelott.Repositories;

import com.example.royaumedekaamelott.Entities.ParticipationQueteEntity;
import com.example.royaumedekaamelott.Entities.ParticipationQueteId;
import com.example.royaumedekaamelott.Enumeration.StatutParticipation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface ParticipantQueteRepository extends JpaRepository<ParticipationQueteEntity, ParticipationQueteId> {
    List<ParticipationQueteEntity> findByQuete_Id(Integer queteId);
    List<ParticipationQueteEntity> findByChevalier_IdAndStatutParticipation(Integer chevalierId, StatutParticipation statutParticipation);

    @Transactional
    @Modifying
    void deleteByChevalier_IdAndQuete_Id(Integer chevalierId, Integer queteId);

}

