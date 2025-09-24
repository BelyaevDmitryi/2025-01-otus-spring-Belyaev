package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.StorageInfoDto;
import ru.otus.hw.services.StorageBookService;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RequiredArgsConstructor
@RestController
public class StorageBookControllerRest {

    private final StorageBookService storageBookService;

    @GetMapping("/storage/api/book/{id}")
    public ResponseEntity<StorageInfoDto> getStorageBookById(@PathVariable("id") Long id) {
        if (giveAnswer()) {
            delay();
            return ResponseEntity.ok(storageBookService.findInfo(id));
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    private void delay() {
        int sleepDuration = ThreadLocalRandom.current().nextInt(500, 1000);
        log.info("sleep {} ms", sleepDuration);

        try {
            Thread.sleep(sleepDuration);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean giveAnswer() {
        int value = ThreadLocalRandom.current().nextInt(30, 100);
        return (value > 50);
    }
}
