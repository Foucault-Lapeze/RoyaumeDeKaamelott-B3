package com.example.royaumedekaamelott.Controller;

import com.example.royaumedekaamelott.Dto.ChevalierDto;
import com.example.royaumedekaamelott.Dto.ParticipantQueteDto;
import com.example.royaumedekaamelott.Entities.ChevalierEntity;
import com.example.royaumedekaamelott.Entities.ParticipationQueteEntity;
import com.example.royaumedekaamelott.Services.ChevalierService;
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
}
