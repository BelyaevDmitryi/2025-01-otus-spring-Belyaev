package ru.otus.hw.util;

import ru.otus.hw.domain.Candidate;
import ru.otus.hw.service.HiringServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Вспомогательные методы для работы с интервью.
 */
public class InterviewUtils {

    /**
     * Создаёт запросы для технического интервью с руководителем и 3 работниками.
     */
    public static List<HiringServiceImpl.TechnicalInterviewRequest> createInterviewRequests(Candidate candidate) {
        List<HiringServiceImpl.TechnicalInterviewRequest> requests = new ArrayList<>();
        requests.add(new HiringServiceImpl.TechnicalInterviewRequest(candidate, "Supervisor"));
        requests.add(new HiringServiceImpl.TechnicalInterviewRequest(candidate, "TechWorker1"));
        requests.add(new HiringServiceImpl.TechnicalInterviewRequest(candidate, "TechWorker2"));
        requests.add(new HiringServiceImpl.TechnicalInterviewRequest(candidate, "TechWorker3"));
        return requests;
    }
}
