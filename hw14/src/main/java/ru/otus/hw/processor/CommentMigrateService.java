package ru.otus.hw.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.mongo.MongoComment;
import ru.otus.hw.models.relation.Comment;
import ru.otus.hw.repositories.relation.CommentRepository;

@RequiredArgsConstructor
@Service
public class CommentMigrateService {

    private final CommentRepository commentRepository;

    private final BookMigrateService bookMigrateService;

    public Comment save(MongoComment mongoComment) {
        Comment comment = new Comment();
        comment.setText(mongoComment.getText());
        comment.setBook(bookMigrateService.getBookByMongoId(mongoComment.getBook().getId()));

        return commentRepository.save(comment);
    }
}
