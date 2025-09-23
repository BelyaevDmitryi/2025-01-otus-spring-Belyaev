package ru.otus.hw.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentSaveDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.mapper.CommentMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

@RequiredArgsConstructor
@RestController
public class CommentControllerRest {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentMapper commentMapper;

    @GetMapping(value = "/api/books/{id}/comments")
    public Flux<CommentDto> commentByBookId(@PathVariable("id") String id) {
        return bookRepository.findById(id)
                .switchIfEmpty(getBook(id))
                .flatMapMany(book -> commentRepository.findByBookId(id))
                .map(commentMapper::fromModel);
    }

    @GetMapping(value = "/api/comments/{id}")
    public Mono<ResponseEntity<CommentDto>> commentById(@PathVariable("id") String id) {
        return commentRepository.findById(id)
                .map(commentMapper::fromModel)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/api/comment")
    public Mono<ResponseEntity<CommentDto>> createComment(@Valid @RequestBody CommentSaveDto commentSaveDto) {
        return bookRepository.findById(commentSaveDto.getBookId())
                .switchIfEmpty(getBook(commentSaveDto.getBookId()))
                .flatMap(book -> {
                    Comment comment = new Comment(null, commentSaveDto.getText(), book);
                    return commentRepository.save(comment)
                            .map(commentMapper::fromModel)
                            .map(ResponseEntity::ok);
                });
    }

    @PutMapping("/api/comment")
    public Mono<ResponseEntity<CommentDto>> updateComment(@Valid @RequestBody CommentSaveDto commentSaveDto) {
        return commentRepository.findById(commentSaveDto.getId())
                .switchIfEmpty(getComment(commentSaveDto.getId()))
                .flatMap(comment -> {
                    comment.setText(commentSaveDto.getText());
                    return commentRepository.save(comment)
                            .map(commentMapper::fromModel)
                            .map(ResponseEntity::ok);
                });
    }

    @DeleteMapping("/api/comment/{id}")
    public Mono<ResponseEntity<Void>> deleteComment(@PathVariable("id") String id) {
        return commentRepository.deleteById(id).then(Mono.just(ResponseEntity.ok().build()));
    }
    private static Mono<Book> getBook(String id) {
        return Mono.error(new NotFoundException("Book with id %s not found".formatted(id)));
    }

    private static Mono<Comment> getComment(String id) {
        return Mono.error(new NotFoundException("Comment with id %s not found".formatted(id)));
    }
}
