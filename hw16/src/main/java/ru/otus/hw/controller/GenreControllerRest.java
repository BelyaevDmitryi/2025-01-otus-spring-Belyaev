package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class GenreControllerRest {

    private final GenreService genreService;

    @GetMapping("/api/genre")
    public ResponseEntity<List<GenreDto>> getAllGenre() {
        return new ResponseEntity<>(genreService.findAll(), HttpStatus.OK);
    }
}
