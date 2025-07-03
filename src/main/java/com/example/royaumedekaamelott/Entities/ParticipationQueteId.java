package com.example.royaumedekaamelott.Entities;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class ParticipationQueteId implements Serializable {
    private Integer chevalier;
    private Integer quete;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ParticipationQueteId that = (ParticipationQueteId) o;
        return Objects.equals(chevalier, that.chevalier) && Objects.equals(quete, that.quete);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chevalier, quete);
    }
}

