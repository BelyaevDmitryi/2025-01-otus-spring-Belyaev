package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.PublishSubscribeChannelSpec;
import org.springframework.integration.store.MessageGroupStore;
import org.springframework.integration.store.SimpleMessageStore;
import org.springframework.messaging.MessageChannel;
import ru.otus.hw.domain.Candidate;
import ru.otus.hw.domain.HiringStatistics;
import ru.otus.hw.domain.TechnicalInterviewResult;
import ru.otus.hw.domain.Worker;
import ru.otus.hw.service.HiringService;
import ru.otus.hw.util.InterviewUtils;

import java.util.List;
import java.util.concurrent.Executors;

@Configuration
public class HiringIntegrationConfig {

    @Bean
    public HiringStatistics hiringStatistics() {
        return new HiringStatistics();
    }

    // Канал для HR-интервью
    @Bean
    public MessageChannelSpec<?, ?> hrInterviewChannel() {
        return MessageChannels.queue(1);
    }

    // Канал для технического интервью с асинхронным исполнением
    @Bean
    public PublishSubscribeChannelSpec<?> technicalInterviewChannel() {
        return MessageChannels.publishSubscribe(Executors.newCachedThreadPool());
    }

    // Канал для сбора результатов технического интервью
    @Bean
    public MessageChannelSpec<?, ?> technicalResultsChannel() {
        return MessageChannels.queue(1);
    }

    // Хранилище групп сообщений для агрегатора
    @Bean
    public MessageGroupStore messageGroupStore() {
        return new SimpleMessageStore();
    }

    @Bean
    public MessageChannelSpec<?, ?> statsTriggerChannel() {
        return MessageChannels.queue();
    }

    @Bean("statisticsChannel")
    public MessageChannel statisticsChannel() {
        return MessageChannels.queue().getObject();
    }

    // Основной поток: HR интервью -> фильтр -> техническое интервью
    @Bean
    public IntegrationFlow hiringFlow(HiringService hiringService) {
        return IntegrationFlow.from("hrInterviewChannel")
                .handle(hiringService, "processHrInterview")
                .<Candidate, Boolean>route(
                        Candidate::isPassedHr,
                        mapping -> mapping
                                .subFlowMapping(true, sf -> sf.channel("technicalInterviewChannel"))
                                .subFlowMapping(false, sf -> sf.handle(m -> {
                                    Candidate c = (Candidate) m.getPayload();
                                    System.out.println("Кандидат " + c.getId() + " отклонен на HR этапе");
                                }))
                )
                .get();
    }

    // Поток технического интервью: split на интервьюеров, обработка каждого, отправка результатов
    @Bean
    public IntegrationFlow technicalInterviewFlow(HiringService hiringService) {
        return IntegrationFlow.from("technicalInterviewChannel")
                .split(Candidate.class, InterviewUtils::createInterviewRequests)
                .handle(hiringService, "processTechnicalInterview")
                .channel("technicalResultsChannel")
                .get();
    }

    // Агрегатор: собираем результаты для каждого кандидата, ждем всех оценок
    @Bean
    public IntegrationFlow aggregateResultsFlow(HiringService hiringService) {
        return IntegrationFlow.from("technicalResultsChannel")
                .aggregate(agg -> agg
                        .correlationStrategy(m -> ((TechnicalInterviewResult) m.getPayload()).getCandidateId())
                        .releaseStrategy(g -> g.size() == expectedInterviewersCount())
                        .messageStore(messageGroupStore())
                )
                .handle(message -> finalizeHiring(message, hiringService))
                .get();
    }

    private int expectedInterviewersCount() {
        return 4; // руководитель + 3 интервьюера
    }

    // Финальный шаг найма
    private void finalizeHiring(org.springframework.messaging.Message<?> message, HiringService hiringService) {
        @SuppressWarnings("unchecked")
        List<TechnicalInterviewResult> results = (List<TechnicalInterviewResult>) message.getPayload();
        Candidate candidate = fetchCandidateById(results.get(0).getCandidateId());
        Worker supervisor = hiringService.findSupervisor(candidate);

        Worker hiredWorker = hiringService.finalizeHiring(results, supervisor);
        if (hiredWorker != null) {
            System.out.println("Кандидат " + hiredWorker.getId() + " принят на работу под руководством " + supervisor.getId());
        } else {
            System.out.println("Кандидат " + candidate.getId() + " не прошел техническое интервью.");
        }
        if (hiringService.getCounter() == (hiringStatistics().getAccepted() + hiringStatistics().getHrRejected() + hiringStatistics().getTechnicalRejected())) {
            System.out.println("=== Итог статистики ===");
            System.out.println(hiringService.getStatisticsReport());
        }
    }

    // Заглушка поиска кандидата по id (можно связать с БД)
    private Candidate fetchCandidateById(String candidateId) {
        // В реальной реализации – поиск кандидата в репозитории
        return new Candidate(candidateId, 4);
    }
}
