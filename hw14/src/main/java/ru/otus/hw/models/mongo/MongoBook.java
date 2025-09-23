package ru.otus.hw.models.mongo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "book")
public class MongoBook {

    @Id
    private String id;

    private String title;

    private MongoAuthor author;

    private MongoGenre genre;

    public MongoBook(String title, MongoAuthor author, MongoGenre genre) {
        this.title = title;
        this.author = author;
        this.genre = genre;
    }
}
