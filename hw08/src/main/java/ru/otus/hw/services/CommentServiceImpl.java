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

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Override
    public Optional<Comment> findById(String id) {
        return commentRepository.findById(id);
    }

    @Override
    public List<Comment> findByBookId(String bookId) {
        getBook(bookId);
        return commentRepository.findByBook(bookId);
    }

    @Override
    @Transactional
    public Comment insert(String bookId, String commentText) {
        var book = getBook(bookId);
        var comment = new Comment(commentText, book);
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public Comment update(String id, String text) {
        var comment = getComment(id);
        comment.setText(text);
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }

    private Book getBook(String id) {
        return bookRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(id)));
    }

    private Comment getComment(String id) {
        return commentRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Comment with id %s not found".formatted(id)));
    }
}
