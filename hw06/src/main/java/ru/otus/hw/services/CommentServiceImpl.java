package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Override
    public Optional<Comment> findById(long id) {
        return commentRepository.findById(id);
    }

    @Override
    public List<Comment> findByBookId(long bookId) {
        getBook(bookId);
        return commentRepository.findByBookId(bookId);
    }

    @Override
    @Transactional
    public Comment insert(long bookId, String commentText) {
        var book = getBook(bookId);
        var comment = new Comment(0, commentText, book);
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public Comment update(long id, String commentText) {
        var comment = commentRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Comment with id %d not found".formatted(id)));
        comment.setText(commentText);
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    private Book getBook(long bookId) {
        return bookRepository.findById(bookId).
                orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
    }
}
