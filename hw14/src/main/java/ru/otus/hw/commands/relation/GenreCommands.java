package ru.otus.hw.commands.relation;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.relation.GenreConverter;
import ru.otus.hw.repositories.relation.GenreRepository;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent(value = "genre commands")
public class GenreCommands {

    private final GenreRepository genreRepository;

    private final GenreConverter genreConverter;

    @ShellMethod(value = "Find all genres", key = "ag")
    public String findAllGenres() {
        return genreRepository.findAll().stream()
                .map(genreConverter::genreToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }
}
