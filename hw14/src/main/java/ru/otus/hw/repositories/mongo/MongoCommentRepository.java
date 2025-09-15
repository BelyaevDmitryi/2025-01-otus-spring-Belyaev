package ru.otus.hw.repositories.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.hw.models.mongo.MongoComment;

import java.util.List;

public interface MongoCommentRepository extends MongoRepository<MongoComment, String> {

    List<MongoComment> findByBookId(@Param("bookId") String bookId);
}
