package com.example.royaumedekaamelott.Repositories;

import com.example.royaumedekaamelott.Entities.ChevalierEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChevalierRepository extends JpaRepository<ChevalierEntity, Integer> {

    List<ChevalierEntity> findByCaracteristiquePrincipale(String caracteristiquePrincipale);

}
