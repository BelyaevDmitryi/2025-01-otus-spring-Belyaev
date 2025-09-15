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
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookSaveDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

@RequiredArgsConstructor
@RestController
public class BookControllerRest {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    private final BookMapper bookMapper;

    @GetMapping("/api/books")
    public Flux<BookDto> getAllBooks() {
        return bookRepository.findAll().map(bookMapper::fromModel);
    }

    @GetMapping("/api/book/{id}")
    public Mono<ResponseEntity<BookDto>> getBookById(@PathVariable String id) {
        return bookRepository.findById(id)
                .map(bookMapper::fromModel)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/api/book")
    public Mono<ResponseEntity<BookDto>> createBook(@Valid @RequestBody BookSaveDto bookSaveDto) {
        return authorRepository.findById(bookSaveDto.getAuthorId())
                .switchIfEmpty(getAuthor(bookSaveDto.getAuthorId()))
                .flatMap(author ->
                        genreRepository.findById(bookSaveDto.getGenreId())
                                .switchIfEmpty(getGenre(bookSaveDto.getGenreId()))
                                .flatMap(genre -> {
                                    Book newbook = new Book(bookSaveDto.getTitle(), author, genre);
                                    return bookRepository.save(newbook)
                                            .map(bookMapper::fromModel)
                                            .map(ResponseEntity::ok);
                                })
                );
    }

    @PutMapping("/api/book")
    public Mono<ResponseEntity<BookDto>> updateBook(@Valid @RequestBody BookSaveDto bookSaveDto) {
        return bookRepository.findById(bookSaveDto.getId())
                .switchIfEmpty(getBook(bookSaveDto.getId()))
                .flatMap(book -> authorRepository.findById(bookSaveDto.getAuthorId())
                        .switchIfEmpty(getAuthor(bookSaveDto.getAuthorId()))
                        .flatMap(author -> genreRepository.findById(bookSaveDto.getGenreId())
                                .switchIfEmpty(getGenre(bookSaveDto.getGenreId()))
                                .flatMap(genre -> {
                                    book.setAuthor(author);
                                    book.setGenre(genre);
                                    book.setTitle(bookSaveDto.getTitle());
                                    return bookRepository.save(book)
                                            .map(bookMapper::fromModel)
                                            .map(ResponseEntity::ok);
                                })
                        )
                );
    }

    @DeleteMapping(value = "/api/book/{id}")
    public Mono<ResponseEntity<Void>> deleteBook(@PathVariable String id) {
        return bookRepository.deleteById(id)
                .then(commentRepository.deleteByBookId(id))
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

    private Mono<Genre> getGenre(String genreId) {
        return Mono.error(new NotFoundException("Genre with id %s not found".formatted(genreId)));
    }

    private Mono<Author> getAuthor(String authorId) {
        return Mono.error(new NotFoundException("Author with id %s not found".formatted(authorId)));
    }

    private Mono<Book> getBook(String id) {
        return Mono.error(new NotFoundException("Book with id %s not found".formatted(id)));
    }
}
