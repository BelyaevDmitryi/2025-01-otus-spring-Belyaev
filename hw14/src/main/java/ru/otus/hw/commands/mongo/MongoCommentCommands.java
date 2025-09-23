package ru.otus.hw.commands.mongo;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.mongo.MongoCommentConverter;
import ru.otus.hw.repositories.mongo.MongoCommentRepository;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent(value = "Mongo book comment commands")
public class MongoCommentCommands {

    private final MongoCommentRepository mongoCommentRepository;

    private final MongoCommentConverter mongoCommentConverter;

    @ShellMethod(value = "Find comment by book id", key = "mo-cbbid")
    public String findCommentByBookId(String bookId) {
        return mongoCommentRepository.findByBookId(bookId).stream()
                .map(mongoCommentConverter::commentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }
    
}
