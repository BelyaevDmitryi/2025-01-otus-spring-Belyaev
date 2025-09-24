package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.otus.hw.service.HiringService;

@Component
@RequiredArgsConstructor
public class AppRunner implements CommandLineRunner {

    private final HiringService hiringService;

    @Override
    public void run(String... args) throws Exception {
        hiringService.setCounter(10);
        hiringService.submitMultipleCandidates();
    }
}
