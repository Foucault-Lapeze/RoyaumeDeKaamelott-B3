package com.example.royaumedekaamelott.Controller;

import com.example.royaumedekaamelott.Dto.AssignationChevalierDto;
import com.example.royaumedekaamelott.Dto.ParticipantQueteDto;
import com.example.royaumedekaamelott.Services.QueteService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
