package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

//    @Query("select bc from Comment bc where bc.book.id = :bookId")
    List<Comment> findByBookId(long bookId);
}
