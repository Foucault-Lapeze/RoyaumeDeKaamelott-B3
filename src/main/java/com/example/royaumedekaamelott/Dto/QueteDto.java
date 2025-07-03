package com.example.royaumedekaamelott.Dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class QueteDto {
    private Integer id;

    private String nomQuete;

    private String description;

    private String difficulte;

    private LocalDate date_assignation;

    private LocalDate date_echeance;
}
