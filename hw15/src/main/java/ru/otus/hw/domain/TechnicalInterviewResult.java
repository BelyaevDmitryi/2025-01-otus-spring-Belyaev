package ru.otus.hw.domain;

public class TechnicalInterviewResult {
    private final String candidateId;
    private final String interviewer;
    private final boolean approved;

    public TechnicalInterviewResult(String candidateId, String interviewer, boolean approved) {
        this.candidateId = candidateId;
        this.interviewer = interviewer;
        this.approved = approved;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public String getInterviewer() {
        return interviewer;
    }

    public boolean isApproved() {
        return approved;
    }
}
