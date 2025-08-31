package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
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

    private final AclServiceWrapperService aclServiceWrapperService;

    @Override
    public CommentDto findById(long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        return comment.map(commentMapper::fromModel).orElseThrow(()
                -> new NotFoundException("Comment with id %d not found".formatted(id)));
    }

    @Override
    @PostFilter("hasPermission(filterObject, 'read')")
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
        aclServiceWrapperService.createReadAndWritePermission(commentMapper.fromModel(comment));
        return commentMapper.fromModel(comment);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_COMMENT_EDITOR') || hasPermission(#commentDto, 'write')")
    public CommentDto update(CommentDto commentDto) {
        var comment = commentRepository.findById(commentDto.getId()).orElseThrow(()
                -> new NotFoundException("Comment with id %d not found".formatted(commentDto.getId())));
        comment.setText(commentDto.getText());
        comment = commentRepository.save(comment);
        return commentMapper.fromModel(comment);
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#commentDto, 'write')")
    public void delete(CommentDto commentDto) {
        commentRepository.deleteById(commentDto.getId());
    }

    private Book getBook(long bookId) {
        return bookRepository.findById(bookId).
                orElseThrow(() -> new NotFoundException("Book with id %d not found".formatted(bookId)));
    }
}
