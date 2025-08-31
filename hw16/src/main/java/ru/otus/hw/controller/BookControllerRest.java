package ru.otus.hw.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookSaveDto;
import ru.otus.hw.services.BookService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BookControllerRest {
    private final BookService bookService;

    @GetMapping("/api/books")
    public ResponseEntity<List<BookDto>> getAllBooks() {
        return ResponseEntity.ok(bookService.findAll());
    }

    @GetMapping("/api/book/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable long id) {
        return ResponseEntity.ok(bookService.findById(id));
    }

    @PostMapping("/api/book")
    public ResponseEntity<BookDto> createBook(@Valid @RequestBody BookSaveDto bookSaveDto) {
        var bookDto = bookService.create(bookSaveDto);
        return new ResponseEntity<BookDto>(bookDto, HttpStatus.CREATED);
    }

    @PutMapping("/api/book")
    public ResponseEntity<BookDto> updateBook(@Valid @RequestBody BookSaveDto bookSaveDto) {
        var bookDto = bookService.update(bookSaveDto);
        return ResponseEntity.ok(bookDto);
    }

    @DeleteMapping(value = "/api/book/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable long id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
