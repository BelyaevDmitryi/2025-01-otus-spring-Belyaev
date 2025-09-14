package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.mapper.AuthorMapper;
import ru.otus.hw.repositories.AuthorRepository;

@RequiredArgsConstructor
@RestController
public class AuthorControllerRest {
    private final AuthorMapper authorMapper;

    private final AuthorRepository authorRepository;

    @GetMapping("/api/author")
    public Flux<AuthorDto> getAllAuthor() {
        return authorRepository.findAll()
                .map(authorMapper::fromModel);
    }

}
