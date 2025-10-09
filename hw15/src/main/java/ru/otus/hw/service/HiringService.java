package ru.otus.hw.service;

import ru.otus.hw.domain.Candidate;
import ru.otus.hw.domain.TechnicalInterviewResult;
import ru.otus.hw.domain.Worker;

import java.util.List;

/**
 * Интерфейс бизнес-сервиса найма.
 */
public interface HiringService {

    void submitMultipleCandidates();

    void submitCandidate(Candidate candidate);

    Candidate processHrInterview(Candidate candidate);

    TechnicalInterviewResult processTechnicalInterview(TechnicalInterviewRequest request);

    boolean makeFinalDecision(List<TechnicalInterviewResult> results);

    Worker finalizeHiring(List<TechnicalInterviewResult> results, Worker supervisor);

    Candidate findCandidate(String candidateId);

    Worker findSupervisor(Candidate candidate);

    public String getStatisticsReport();

    public int getCounter();

    public void setCounter(int counter);

    /** Вложенный класс-запрос технического интервью */
    class TechnicalInterviewRequest {
        private Candidate candidate;
        private String interviewer;

        public TechnicalInterviewRequest(Candidate candidate, String interviewer) {
            this.candidate = candidate;
            this.interviewer = interviewer;
        }

        public Candidate getCandidate() { return candidate; }
        public String getInterviewer() { return interviewer; }
    }
}
