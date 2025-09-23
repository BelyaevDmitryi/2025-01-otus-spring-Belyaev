package ru.otus.hw.converters.mongo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.mongo.MongoBook;

@RequiredArgsConstructor
@Component
public class MongoBookConverter {
    private final MongoAuthorConverter mongoAuthorConverter;

    private final MongoGenreConverter mongoGenreConverter;

    public String bookToString(MongoBook book) {
        return "Id: %s, title: %s, author: {%s}, genres: [%s]".formatted(
                book.getId(),
                book.getTitle(),
                mongoAuthorConverter.authorToString(book.getAuthor()),
                mongoGenreConverter.genreToString(book.getGenre()));
    }

}
