package ru.otus.hw.converters.mongo;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.mongo.MongoGenre;

@Component
public class MongoGenreConverter {
    public String genreToString(MongoGenre genre) {
        return "Id: %s, Name: %s".formatted(genre.getId(), genre.getName());
    }

}
