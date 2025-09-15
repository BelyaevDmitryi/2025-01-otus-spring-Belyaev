package ru.otus.hw.commands.mongo;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.mongo.MongoAuthorConverter;
import ru.otus.hw.repositories.mongo.MongoAuthorRepository;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent(value = "Mongo author commands")
public class MongoAuthorCommands {

    private final MongoAuthorRepository mongoAuthorRepository;

    private final MongoAuthorConverter mongoAuthorConverter;

    @ShellMethod(value = "Find all authors", key = "mo-aa")
    public String findAllAuthors() {
        return mongoAuthorRepository.findAll().stream()
                .map(mongoAuthorConverter::authorToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

}
