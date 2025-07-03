package com.example.royaumedekaamelott.Services;

import com.example.royaumedekaamelott.Dto.AssignationChevalierDto;
import com.example.royaumedekaamelott.Dto.ParticipantQueteDto;
import com.example.royaumedekaamelott.Dto.QueteDto;
import com.example.royaumedekaamelott.Entities.ChevalierEntity;
import com.example.royaumedekaamelott.Entities.ParticipationQueteEntity;
import com.example.royaumedekaamelott.Entities.QueteEntity;
import com.example.royaumedekaamelott.Enumeration.Difficulte;
import com.example.royaumedekaamelott.Enumeration.Role;
import com.example.royaumedekaamelott.Enumeration.StatutParticipation;
import com.example.royaumedekaamelott.Repositories.ChevalierRepository;
import com.example.royaumedekaamelott.Repositories.ParticipantQueteRepository;
import com.example.royaumedekaamelott.Repositories.QueteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QueteService {
    @Autowired
    private ParticipantQueteRepository participantQueteRepository;

    @Autowired
    private ChevalierRepository chevalierRepository;

    @Autowired
    private QueteRepository queteRepository;

    public List<ParticipantQueteDto> getParticipantsByQueteId(Integer queteId) {
        List<ParticipationQueteEntity> participations = participantQueteRepository.findByQuete_Id(queteId);
        if (participations.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucun participant trouvé pour la quête avec l'ID " + queteId);
        }

        return participations.stream().map(participation -> {
            ParticipantQueteDto dto = new ParticipantQueteDto();
            dto.setChevalierId(participation.getChevalier().getId());
            dto.setNom(participation.getChevalier().getNom());
            dto.setTitre(participation.getChevalier().getTitre());
            dto.setCaracteristiquePrincipale(participation.getChevalier().getCaracteristiquePrincipale());
            dto.setNiveauBravoure(participation.getChevalier().getNiveauBravoure());
            dto.setRole(participation.getRole().toString());
            dto.setStatutParticipation(participation.getStatutParticipation().toString());
            dto.setCommentaire(participation.getCommentaireRoi());
            return dto;
        }).collect(Collectors.toList());
    }

    public void assignerChevalier(Integer idQuete, AssignationChevalierDto dto) {
        ChevalierEntity chevalier = chevalierRepository.findById(dto.getIdChevalier())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chevalier introuvable"));

        QueteEntity quete = queteRepository.findById(idQuete)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quête introuvable"));

        Role role;
        StatutParticipation statut;

        try {
            role = Role.valueOf(dto.getRole().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rôle invalide : " + dto.getRole());
        }

        try {
            statut = StatutParticipation.valueOf(dto.getStatutParticipation().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Statut de participation invalide : " + dto.getStatutParticipation());
        }

        ParticipationQueteEntity participation = new ParticipationQueteEntity();
        participation.setChevalier(chevalier);
        participation.setQuete(quete);
        participation.setRole(role);
        participation.setStatutParticipation(statut);
        participation.setCommentaireRoi(dto.getCommentaireRoi());

        participantQueteRepository.save(participation);
    }

    public List<QueteDto> getQuetesEnCoursByChevalierId(Integer idChevalier) {
        List<ParticipationQueteEntity> participations = participantQueteRepository
                .findByChevalier_IdAndStatutParticipation(idChevalier, StatutParticipation.EN_COURS);

        if (participations.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucune quête en cours trouvée pour ce chevalier");
        }

        // Convertir ParticipationQueteEntity -> QueteDto
        return participations.stream()
                .map(participation -> {
                    QueteEntity quete = participation.getQuete();
                    QueteDto dto = new QueteDto();
                    dto.setId(quete.getId());
                    dto.setNomQuete(quete.getNomQuete());
                    dto.setDescription(quete.getDescription());
                    dto.setDifficulte(quete.getDifficulte().toString());
                    dto.setDate_assignation(quete.getDateAssignation());
                    dto.setDate_echeance(quete.getDateEcheance());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<QueteEntity> getQuetesAberranteNonCommenceeOuEnCours() {
        return queteRepository.findAberranteNonCommenceeOuEnCours(
                Difficulte.ABERRANTE,
                StatutParticipation.EN_COURS);
    }
}
