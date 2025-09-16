package ru.otus.hw.converters.mongo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.mongo.MongoComment;

@RequiredArgsConstructor
@Component
public class MongoCommentConverter {

    public String commentToString(MongoComment comment) {
        return "Id: %s, comment: %s".formatted(
                comment.getId(),
                comment.getText());
    }

}
