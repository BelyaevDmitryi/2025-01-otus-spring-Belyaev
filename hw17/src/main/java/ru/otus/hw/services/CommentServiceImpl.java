package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentSaveDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.mapper.CommentMapper;
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

    private final CommentMapper commentMapper;

    @Override
    public CommentDto findById(long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        return comment.map(commentMapper::fromModel).orElseThrow(()
                -> new NotFoundException("Comment with id %d not found".formatted(id)));
    }

    @Override
    public List<CommentDto> findByBookId(long bookId) {
        getBook(bookId);
        return commentRepository.findByBookId(bookId).stream()
                .map(commentMapper::fromModel).toList();
    }

    @Override
    @Transactional
    public CommentDto create(CommentSaveDto commentSaveDto) {
        var book = getBook(commentSaveDto.getBookId());
        var comment = new Comment(0, commentSaveDto.getText(), book);
        comment = commentRepository.save(comment);
        return commentMapper.fromModel(comment);
    }

    @Override
    @Transactional
    public CommentDto update(CommentSaveDto commentSaveDto) {
        var comment = commentRepository.findById(commentSaveDto.getId()).orElseThrow(()
                -> new NotFoundException("Comment with id %d not found".formatted(commentSaveDto.getId())));
        comment.setText(commentSaveDto.getText());
        comment = commentRepository.save(comment);
        return commentMapper.fromModel(comment);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    private Book getBook(long bookId) {
        return bookRepository.findById(bookId).
                orElseThrow(() -> new NotFoundException("Book with id %d not found".formatted(bookId)));
    }
}
