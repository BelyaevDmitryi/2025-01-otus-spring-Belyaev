package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public Book findById(long id) {
        return bookRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional
    public Book insert(Book book) {
        Author author = authorRepository.findById(book.getAuthor().getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Author with id %d not found".formatted(book.getAuthor().getId())));

        Genre genre = genreRepository.findById(book.getGenre().getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Genre with id %d not found".formatted(book.getGenre().getId())));
        book.setAuthor(author);
        book.setGenre(genre);

        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public Book update(Book book) {
        Book existingBook = bookRepository.findById(book.getId()).orElseThrow(() ->
                new EntityNotFoundException("Book with id %d not found".formatted(book.getId())));

        Author author = authorRepository.findById(book.getAuthor().getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Author with id %d not found".formatted(book.getAuthor().getId())));
        Genre genre = genreRepository.findById(book.getGenre().getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Genre with id %d not found".formatted(book.getGenre().getId())));

        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(author);
        existingBook.setGenre(genre);

        return bookRepository.save(existingBook);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }
}
