package com.example.royaumedekaamelott.Repositories;

import com.example.royaumedekaamelott.Entities.ParticipationQueteEntity;
import com.example.royaumedekaamelott.Entities.ParticipationQueteId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantQueteRepository extends JpaRepository<ParticipationQueteEntity, ParticipationQueteId> {
    List<ParticipationQueteEntity> findByQuete_Id(Integer queteId);
}

