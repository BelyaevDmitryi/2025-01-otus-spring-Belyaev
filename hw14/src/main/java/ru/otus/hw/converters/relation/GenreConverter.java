package ru.otus.hw.converters.relation;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.relation.Genre;

@Component
public class GenreConverter {
    public String genreToString(Genre genre) {
        return "Id: %d, Name: %s".formatted(genre.getId(), genre.getName());
    }

}
