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
@Document(value = "author")
public class MongoAuthor {

    @Id
    private String id;

    private String fullName;

    public MongoAuthor(String fullName) {
        this.fullName = fullName;
    }
}
