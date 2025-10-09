package ru.otus.hw.domain;


import lombok.Getter;
import lombok.Setter;

public class Worker {
    @Getter
    private String id;
    @Getter
    @Setter
    private String status;
    @Setter
    @Getter
    private Worker supervisor;
    private Candidate candidate;

    public Worker(Candidate candidate) {
        this.candidate = candidate;
        this.id = candidate.getId();
    }
}
