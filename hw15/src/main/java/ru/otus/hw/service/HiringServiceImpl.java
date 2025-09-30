package ru.otus.hw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Candidate;
import ru.otus.hw.domain.HiringStatistics;
import ru.otus.hw.domain.TechnicalInterviewResult;
import ru.otus.hw.domain.Worker;

import java.util.List;
import java.util.Random;

@Service
public class HiringServiceImpl implements HiringService {

    private final MessageChannel hrInterviewChannel;

    private int counter;

    @Autowired
    @Qualifier("statisticsChannel")
    private MessageChannel statisticsChannel;

    @Autowired
    private HiringStatistics hiringStatistics;

    @Autowired
    public HiringServiceImpl(@Qualifier("hrInterviewChannel") MessageChannel hrInterviewChannel) {
        this.hrInterviewChannel = hrInterviewChannel;
    }

    private final Random random = new Random();

    @Override
    public void submitMultipleCandidates() {
        counter = 3;
        for (int i = 1; i <= counter; i++) {
            int qualLevel = 1 + random.nextInt(5); // От 1 до 5
            Candidate candidate = new Candidate("cand" + String.format("%03d", i), qualLevel);
            submitCandidate(candidate);
        }
    }

    @Override
    public void submitCandidate(Candidate candidate) {
        hrInterviewChannel.send(MessageBuilder.withPayload(candidate).build());
    }

    @Override
    public Candidate processHrInterview(Candidate candidate) {
        if (candidate.getQualificationLevel() >= 3) {
            candidate.setPassedHr(true);
        } else {
            candidate.setPassedHr(false);
            statisticsChannel.send(MessageBuilder.withPayload("HR_REJECTED").build());
            hiringStatistics.incrementHrRejected();
        }
        return candidate;
    }

    @Override
    public TechnicalInterviewResult processTechnicalInterview(TechnicalInterviewRequest request) {
        boolean approved = performTechnicalCheck(request.getCandidate(), request.getInterviewer());
        return new TechnicalInterviewResult(request.getCandidate().getId(), request.getInterviewer(), approved);
    }

    @Override
    public boolean makeFinalDecision(List<TechnicalInterviewResult> results) {
        long countApproved = results.stream().filter(TechnicalInterviewResult::isApproved).count();
        return countApproved > results.size() / 2;
    }

    @Override
    public Worker finalizeHiring(List<TechnicalInterviewResult> results, Worker supervisor) {
        if (makeFinalDecision(results)) {
            Candidate candidate = findCandidate(results.get(0).getCandidateId());
            Worker worker = new Worker(candidate);
            worker.setStatus("Принят");
            worker.setSupervisor(supervisor);
            hiringStatistics.incrementAccepted();
            return worker;
        } else {
            hiringStatistics.incrementTechnicalRejected();
            return null;
        }
    }

    @Override
    public Candidate findCandidate(String candidateId) {
        return new Candidate(candidateId, 4);
    }

    @Override
    public Worker findSupervisor(Candidate candidate) {
        return new Worker(new Candidate("supervisor001", 5));
    }

    private boolean performTechnicalCheck(Candidate candidate, String interviewer) {
        return Math.random() > 0.3;
    }

    @Override
    public String getStatisticsReport() {
        return hiringStatistics.toString();
    }

    @Override
    public int getCounter() {
        return counter;
    }

    @Override
    public void setCounter(int counter) {
        this.counter = counter;
    }
}
