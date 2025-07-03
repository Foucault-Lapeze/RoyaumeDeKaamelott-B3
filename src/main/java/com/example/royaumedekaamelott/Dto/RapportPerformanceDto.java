package com.example.royaumedekaamelott.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RapportPerformanceDto {

    private int totalQuetesTerminees;

    private int totalQuetesChefExpedition;

    private double tauxSucces;

    private String commentaireRoiFrequent;
}
