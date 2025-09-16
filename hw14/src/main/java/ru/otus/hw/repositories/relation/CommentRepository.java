package ru.otus.hw.repositories.relation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.otus.hw.models.relation.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT bc FROM Comment bc WHERE bc.book.id = :bookId")
    List<Comment> findByBookId(long bookId);

}
