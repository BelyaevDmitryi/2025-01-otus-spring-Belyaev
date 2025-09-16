package ru.otus.hw.converters.relation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.relation.Comment;

@RequiredArgsConstructor
@Component
public class CommentConverter {

    public String commentToString(Comment comment) {
        return "Id: %d, comment: %s".formatted(
                comment.getId(),
                comment.getText());
    }

}
