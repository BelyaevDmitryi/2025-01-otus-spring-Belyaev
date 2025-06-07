package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.otus.hw.events.CascadeDelete;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Comment {

    @Id
    private String id;

    private String text;

    @DBRef(lazy = true)
    @CascadeDelete
    private Book book;

    public Comment(String text, Book book) {
        this.text = text;
        this.book = book;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", commentText='" + text + '\'' +
                '}';
    }

}
