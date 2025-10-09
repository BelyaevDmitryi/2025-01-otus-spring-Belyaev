package ru.otus.hw.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Candidate {
    private String id;
    private final int qualificationLevel;
    @Setter
    private boolean passedHr;

    public Candidate(String id, int qualificationLevel) {
        this.id = id;
        this.qualificationLevel = qualificationLevel;
        this.passedHr = false;
    }
}
