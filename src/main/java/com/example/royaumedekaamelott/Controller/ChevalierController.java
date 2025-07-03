package com.example.royaumedekaamelott.Controller;

import com.example.royaumedekaamelott.Dto.ChevalierDto;
import com.example.royaumedekaamelott.Entities.ChevalierEntity;
import com.example.royaumedekaamelott.Services.ChevalierService;
import com.example.royaumedekaamelott.Services.QueteService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chevalier")
public class ChevalierController {

    @Autowired
    private ChevalierService chevalierService;

    @Autowired
    private QueteService queteService;

    @Operation(summary = "create chevalier", description = "create chevalier", operationId = "create chevalier")
    @PostMapping("create/chevalier")
    public ResponseEntity<ChevalierDto> createChevalier(@RequestBody ChevalierDto chevalierDto) {
        ChevalierDto newChevalierDto = chevalierService.createChevalier(chevalierDto);
        return new ResponseEntity<>(newChevalierDto, HttpStatus.CREATED);
    }

    @Operation(summary = "Get tous les chevaliers", description = "Get tous les chevaliers", operationId = "Get tous les chevaliers")
    @GetMapping("getAll/")
    public List<ChevalierEntity> getAllChevalier() {
        return chevalierService.getAllChevaliers();
    }


    @DeleteMapping("/chevaliers/{idChevalier}/retirer-quete/{idQuete}")
    @Operation(summary = "Retirer un chevalier d'une quête")
    public ResponseEntity<String> retirerChevalierDeLaQuete(
        @PathVariable Integer idChevalier,
        @PathVariable Integer idQuete) {

        queteService.retirerChevalierDeLaQuete(idChevalier, idQuete);
        return ResponseEntity.ok("Chevalier retiré de la quête avec succès");
    }

    @GetMapping("/chevaliers/caracteristique/{caracteristique}")
    public ResponseEntity<List<ChevalierEntity>> getChevaliersByCaracteristique(@PathVariable String caracteristique) {
        List<ChevalierEntity> chevaliers = chevalierService.findByCaracteristiquePrincipale(caracteristique);

        if (chevaliers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(chevaliers);
    }

}
