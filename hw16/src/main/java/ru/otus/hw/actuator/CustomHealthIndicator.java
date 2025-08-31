package ru.otus.hw.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class CustomHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        return Health.up()
                .withDetail("message", "Library is open")
                .withDetail("time", ZonedDateTime.now())
                .build();
    }
}
