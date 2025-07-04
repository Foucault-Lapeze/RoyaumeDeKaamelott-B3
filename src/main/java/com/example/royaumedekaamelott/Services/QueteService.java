package com.example.royaumedekaamelott.Services;

import com.example.royaumedekaamelott.Dto.*;
import com.example.royaumedekaamelott.Entities.ChevalierEntity;
import com.example.royaumedekaamelott.Entities.ParticipationQueteEntity;
import com.example.royaumedekaamelott.Entities.QueteEntity;
import com.example.royaumedekaamelott.Enumeration.Difficulte;
import com.example.royaumedekaamelott.Enumeration.Role;
import com.example.royaumedekaamelott.Enumeration.StatutParticipation;
import com.example.royaumedekaamelott.Repositories.ChevalierRepository;
import com.example.royaumedekaamelott.Repositories.ParticipantQueteRepository;
import com.example.royaumedekaamelott.Repositories.QueteRepository;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;
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


    @DeleteMapping("/chevaliers/{idChevalier}/retirer-quete/{idQuete}")
    public ResponseEntity<Void> retirerChevalierDeLaQuete(
            @PathVariable Integer idChevalier,
            @PathVariable Integer idQuete
    ) {
        participantQueteRepository.deleteByChevalier_IdAndQuete_Id(idChevalier, idQuete);
        return ResponseEntity.noContent().build();
    }

    public List<QueteEntity> getQuetesAvecEffectifManquant(long minChevaliers) {
        return queteRepository.findQuetesAvecMoinsDeChevaliers(minChevaliers);
    }

    public List<QueteEntity> getQuetesLesPlusLongues(int limit) {
        return queteRepository.findAllOrderByDureeDesc(PageRequest.of(0, limit));
    }

    public List<QuetePeriodeDto> getQuetesParPeriode(LocalDate dateDebut, LocalDate dateFin) {
        List<QueteEntity> quetes = queteRepository.findQuetesParChevauchement(dateDebut, dateFin);

        return quetes.stream().map(q -> {
            QuetePeriodeDto dto = new QuetePeriodeDto();
            dto.setNomQuete(q.getNomQuete());

            // Nombre de chevaliers
            int nbChevaliers = participantQueteRepository.findByQuete_Id(q.getId()).size();
            dto.setNombreChevaliers(nbChevaliers);

            // Durée
            long jours = ChronoUnit.DAYS.between(q.getDateAssignation(), q.getDateEcheance());
            dto.setDureeEnJours(jours);

            // Difficulte
            dto.setDifficulte(q.getDifficulte().toString());

            // Statut global (selon dates)
            LocalDate aujourdHui = LocalDate.now();
            if (aujourdHui.isBefore(q.getDateAssignation())) {
                dto.setStatutGlobal("À Venir");
            } else if (!aujourdHui.isAfter(q.getDateEcheance())) {
                dto.setStatutGlobal("En Cours");
            } else {
                dto.setStatutGlobal("Terminée");
            }

            return dto;
        }).collect(Collectors.toList());
    }

    public RapportPerformanceDto genererRapportPerformance(Integer idChevalier) {
        List<ParticipationQueteEntity> participations = participantQueteRepository.findByChevalier_Id(idChevalier);

        if (participations.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucune participation trouvée.");
        }

        int totalTerminees = 0;
        int totalChefExpedition = 0;
        int totalTermineeEtEnCours = 0;

        Map<String, Integer> commentaireCount = new HashMap<>();

        for (ParticipationQueteEntity p : participations) {
            StatutParticipation statut = p.getStatutParticipation();
            if (statut == StatutParticipation.TERMINEE) totalTerminees++;
            if (statut == StatutParticipation.EN_COURS || statut == StatutParticipation.TERMINEE) totalTermineeEtEnCours++;

            if (p.getRole().name().equals("CHEF_EXPEDITION")) totalChefExpedition++;

            String commentaire = p.getCommentaireRoi();
            if (commentaire != null && !commentaire.isBlank()) {
                commentaireCount.put(commentaire, commentaireCount.getOrDefault(commentaire, 0) + 1);
            }
        }

        String commentaireLePlusFrequent = commentaireCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Aucun commentaire");

        RapportPerformanceDto rapport = new RapportPerformanceDto();
        rapport.setTotalQuetesTerminees(totalTerminees);
        rapport.setTotalQuetesChefExpedition(totalChefExpedition);
        rapport.setTauxSucces(totalTermineeEtEnCours == 0 ? 0 : (double) totalTerminees / totalTermineeEtEnCours);
        rapport.setCommentaireRoiFrequent(commentaireLePlusFrequent);

        return rapport;
    }

    public RapportMensuelDto genererRapportMensuel(int mois, int annee) {
        LocalDate debut = LocalDate.of(annee, mois, 1);
        LocalDate fin = debut.withDayOfMonth(debut.lengthOfMonth());

        // 1. Quêtes initiées
        int nbQuetesInitiees = queteRepository.countByDateAssignationBetween(debut, fin);

        // 2. Participations terminées
        int nbQuetesTerminees = participantQueteRepository.countByStatutParticipationAndDateAssignationBetween(
                StatutParticipation.TERMINEE, debut, fin
        );

        // 3. Chevaliers ayant participé
        List<Integer> chevalierIds = participantQueteRepository.findDistinctChevalierIdsByDateAssignationBetween(debut, fin);
        int nbChevaliersActifs = chevalierIds.size();

        // 4. Quête la plus lamentablement échouée
        List<ParticipationQueteEntity> echecs = participantQueteRepository
                .findByStatutParticipationAndDateAssignationBetween(
                        StatutParticipation.ECHOUEE_LAMENTABLEMENT, debut, fin
                );

        Map<QueteEntity, List<String>> echecsParQuete = new HashMap<>();
        for (ParticipationQueteEntity e : echecs) {
            echecsParQuete.computeIfAbsent(e.getQuete(), q -> new ArrayList<>())
                    .add(e.getChevalier().getNom());
        }

        QueteEntity pireQuete = echecsParQuete.entrySet().stream()
                .max(Comparator.comparingInt(e -> e.getValue().size()))
                .map(Map.Entry::getKey)
                .orElse(null);

        RapportMensuelDto rapport = new RapportMensuelDto();
        rapport.setNbQuetesInitiées(nbQuetesInitiees);
        rapport.setNbQuetesTerminees(nbQuetesTerminees);
        rapport.setNbChevaliersActifs(nbChevaliersActifs);

        if (pireQuete != null) {
            rapport.setQueteLaPlusEchouee(pireQuete.getNomQuete());
            rapport.setChevaliersResponsables(echecsParQuete.get(pireQuete));
        } else {
            rapport.setQueteLaPlusEchouee("Aucune");
            rapport.setChevaliersResponsables(Collections.emptyList());
        }

        return rapport;
    }

}
