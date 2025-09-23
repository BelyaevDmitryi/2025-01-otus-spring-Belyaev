package ru.otus.hw.commands.relation;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.relation.BookConverter;
import ru.otus.hw.repositories.relation.BookRepository;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent(value = "book commands")
public class BookCommands {

    private final BookRepository bookRepository;

    private final BookConverter bookConverter;

    @ShellMethod(value = "Find all books", key = "ab")
    public String findAllBooks() {
        return bookRepository.findAll().stream()
                .map(bookConverter::bookToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

}
