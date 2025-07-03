package com.example.royaumedekaamelott.Dto;

import com.example.royaumedekaamelott.Enumeration.Role;
import com.example.royaumedekaamelott.Enumeration.StatutParticipation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignationChevalierDto {

    private Integer idChevalier;

    private String role;

    private String statutParticipation;

    private String commentaireRoi;
}
