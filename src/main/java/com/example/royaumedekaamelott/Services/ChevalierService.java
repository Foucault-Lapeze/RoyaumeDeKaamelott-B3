package com.example.royaumedekaamelott.Services;

import com.example.royaumedekaamelott.Dto.ChevalierDto;
import com.example.royaumedekaamelott.Dto.ParticipantQueteDto;
import com.example.royaumedekaamelott.Entities.ChevalierEntity;
import com.example.royaumedekaamelott.Entities.ParticipationQueteEntity;
import com.example.royaumedekaamelott.Repositories.ChevalierRepository;
import com.example.royaumedekaamelott.Repositories.ParticipantQueteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChevalierService {

    @Autowired
    private ChevalierRepository chevalierRepository;

    public ChevalierDto createChevalier(ChevalierDto chevalierDto) {

        if (chevalierDto.getNiveauBravoure() < 1 || chevalierDto.getNiveauBravoure() > 10) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le niveau de bravoure doit être entre 1 et 10.");
        }

        // Conversion DTO -> Entity
        ChevalierEntity chevalierEntity = new ChevalierEntity();
        chevalierEntity.setNom(chevalierDto.getNom());
        chevalierEntity.setTitre(chevalierDto.getTitre());
        chevalierEntity.setCaracteristiquePrincipale(chevalierDto.getCaracteristiquePrincipale());
        chevalierEntity.setNiveauBravoure(chevalierDto.getNiveauBravoure());


        ChevalierEntity savedChevalierEntity = chevalierRepository.save(chevalierEntity);

        ChevalierDto savedChevalierDto = new ChevalierDto();
        savedChevalierDto.setNom(savedChevalierEntity.getNom());
        savedChevalierDto.setTitre(savedChevalierEntity.getTitre());
        savedChevalierDto.setCaracteristiquePrincipale(savedChevalierEntity.getCaracteristiquePrincipale());
        savedChevalierDto.setNiveauBravoure(savedChevalierEntity.getNiveauBravoure());

        return savedChevalierDto;
    }

    public List<ChevalierEntity> getAllChevaliers() {
        return chevalierRepository.findAll();
    }

    public List<ChevalierEntity> findByCaracteristiquePrincipale(String caracteristique) {
        return chevalierRepository.findByCaracteristiquePrincipale(caracteristique);
    }
}