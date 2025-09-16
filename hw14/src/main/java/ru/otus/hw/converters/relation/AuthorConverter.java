package ru.otus.hw.converters.relation;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.relation.Author;

@Component
public class AuthorConverter {
    public String authorToString(Author author) {
        return "Id: %d, FullName: %s".formatted(author.getId(), author.getFullName());
    }

}
