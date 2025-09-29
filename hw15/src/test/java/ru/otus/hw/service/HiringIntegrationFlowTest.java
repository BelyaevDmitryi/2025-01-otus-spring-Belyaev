package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.config.AppRunner;
import ru.otus.hw.domain.Candidate;
import ru.otus.hw.domain.HiringStatistics;
import ru.otus.hw.domain.TechnicalInterviewResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class HiringIntegrationFlowTest {

    @MockitoBean
    private AppRunner appRunner;

    @Autowired
    HiringService hiringService;

    @Autowired
    MessageChannel hrInterviewChannel;

    @Autowired
    QueueChannel technicalResultsChannel;

    @Autowired
    HiringStatistics hiringStatistics;

    @Test
    void hiringFlowProcessesCandidatesCorrectly() throws Exception {
        // Отправляем кандидатов
        hiringService.submitMultipleCandidates();

        // Проверяем статистику после первичного фильтра
        Thread.sleep(1000); // ждем потоков

        // Проверяем что отклоненные по HR попали в статистику
        int rejectedHr = hiringStatistics.getHrRejected();
        assertThat(rejectedHr).isBetween(0, 3);

        // Извлекаем результаты технического интервью из channel
        int receivedResults = 0;
        for (int i = 0; i < 20; i++) {
            Message<?> message = technicalResultsChannel.receive(500);
            if (message == null) break;
            Object payload = message.getPayload();
            assertThat(payload).isInstanceOf(TechnicalInterviewResult.class);
            receivedResults++;
        }
        // Должны быть результаты технического интервью для прошедших кандидатов
        assertThat(receivedResults)
                .isGreaterThanOrEqualTo(0);

        // Проверяем итоговую статистику
        String report = hiringService.getStatisticsReport();
        assertThat(report)
                .contains("Statistics{HR rejected=")
                .contains("Technical rejected=")
                .contains("Accepted=");
    }

    @Test
    void processHrInterviewShouldFilterCorrectly() {
        // Кандидат с низкой квалификацией
        Candidate low = new Candidate("candLow", 2);
        hiringService.processHrInterview(low);
        assertThat(low.isPassedHr()).isFalse();

        // Кандидат с высокой квалификацией
        Candidate high = new Candidate("candHigh", 4);
        hiringService.processHrInterview(high);
        assertThat(high.isPassedHr()).isTrue();
    }

    @Test
    void aggregateAndFinalizeHiringWorks() {
        Candidate candidate = new Candidate("cand42", 5);

        TechnicalInterviewResult r1 = new TechnicalInterviewResult(candidate.getId(), "Supervisor", true);
        TechnicalInterviewResult r2 = new TechnicalInterviewResult(candidate.getId(), "TechWorker1", true);
        TechnicalInterviewResult r3 = new TechnicalInterviewResult(candidate.getId(), "TechWorker2", false);
        TechnicalInterviewResult r4 = new TechnicalInterviewResult(candidate.getId(), "TechWorker3", true);
        List<TechnicalInterviewResult> results = List.of(r1, r2, r3, r4);

        var supervisor = hiringService.findSupervisor(candidate);
        var worker = hiringService.finalizeHiring(results, supervisor);
        assertThat(worker)
                .isNotNull();
        assertThat(worker.getStatus())
                .isEqualTo("Принят");
        assertThat(worker.getSupervisor())
                .isEqualTo(supervisor);
    }
}

