package com.example.royaumedekaamelott.Dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RapportMensuelDto {
    private int nbQuetesInitiées;

    private int nbQuetesTerminees;

    private int nbChevaliersActifs;

    private String queteLaPlusEchouee;

    private List<String> chevaliersResponsables;
}
