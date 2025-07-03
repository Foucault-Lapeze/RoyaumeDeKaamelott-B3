package com.example.royaumedekaamelott.Controller;

import com.example.royaumedekaamelott.Dto.*;
import com.example.royaumedekaamelott.Entities.QueteEntity;
import com.example.royaumedekaamelott.Services.QueteService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/quete")
public class QueteController {

    @Autowired
    private QueteService queteService;

    @Operation(summary = "Obtenir les participants d'une quête", description = "Retourne la liste des chevaliers assignés à une quête spécifique avec leur rôle et statut")
    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ParticipantQueteDto>> getParticipantsByQueteId(@PathVariable Integer id) {
        List<ParticipantQueteDto> participants = queteService.getParticipantsByQueteId(id);
        return ResponseEntity.ok(participants);
    }

    @PostMapping("/{idQuete}/assigner-chevalier")
    @Operation(summary = "Assigner un chevalier à une quête")
    public ResponseEntity<String> assignerChevalier(
            @PathVariable Integer idQuete,
            @RequestBody AssignationChevalierDto dto
    ) {
        queteService.assignerChevalier(idQuete, dto);
        return ResponseEntity.ok("Chevalier assigné avec succès !");
    }

    @GetMapping("/chevaliers/{idChevalier}/quetes-en-cours")
    @Operation(summary = "Liste des quêtes en cours pour un chevalier")
    public ResponseEntity<List<QueteDto>> getQuetesEnCoursByChevalierId(@PathVariable Integer idChevalier) {
        List<QueteDto> quetes = queteService.getQuetesEnCoursByChevalierId(idChevalier);
        return ResponseEntity.ok(quetes);
    }

    @GetMapping("/chevaliers/{idChevalier}/difficulte-aberrante")
    @Operation(summary = "Liste des quêtes ou la difficultés est Aberrante")
    public ResponseEntity<List<QueteEntity>> getQuetesDifficulteAberrante() {
        List<QueteEntity> quetes = queteService.getQuetesAberranteNonCommenceeOuEnCours();

        return ResponseEntity.ok(quetes);
    }

    @GetMapping("/quetes/effectif-manquant")
    public ResponseEntity<List<QueteEntity>> getQuetesAvecEffectifManquant(@RequestParam long minChevaliers) {
        List<QueteEntity> quetes = queteService.getQuetesAvecEffectifManquant(minChevaliers);

        if (quetes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(quetes);
    }

    @GetMapping("/quetes/les-plus-longues")
    public ResponseEntity<List<QueteEntity>> getQuetesLesPlusLongues(@RequestParam(defaultValue = "5") int limit) {
        List<QueteEntity> quetes = queteService.getQuetesLesPlusLongues(limit);
        return ResponseEntity.ok(quetes);
    }

    @GetMapping("/quetes/periode")
    public ResponseEntity<List<QuetePeriodeDto>> getQuetesParPeriode(
            @RequestParam("date_debut") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam("date_fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin
    ) {
        List<QuetePeriodeDto> resultats = queteService.getQuetesParPeriode(dateDebut, dateFin);
        return ResponseEntity.ok(resultats);
    }

    @GetMapping("/chevaliers/rapport-performance/{idChevalier}")
    @Operation(summary = "Rapport de performance du chevalier")
    public ResponseEntity<RapportPerformanceDto> getRapportPerformance(@PathVariable Integer idChevalier) {
        RapportPerformanceDto rapport = queteService.genererRapportPerformance(idChevalier);
        return ResponseEntity.ok(rapport);
    }

}
