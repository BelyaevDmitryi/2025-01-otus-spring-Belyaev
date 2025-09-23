package ru.otus.hw.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.migrate.BookMigrate;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.models.relation.Book;
import ru.otus.hw.repositories.relation.BookIdMigrateRepository;
import ru.otus.hw.repositories.relation.BookRepository;

@RequiredArgsConstructor
@Service
public class BookMigrateService {

    private final BookRepository bookRepository;

    private final BookIdMigrateRepository bookIdMigrateRepository;

    private final AuthorMigrateService authorMigrateService;

    private final GenreMigrateService genreMigrateService;

    public Book save(MongoBook mongoBook) {
        Book book = new Book();
        book.setTitle(mongoBook.getTitle());
        book.setAuthor(authorMigrateService.getAuthorByMongoId(mongoBook.getAuthor().getId()));
        book.setGenre(genreMigrateService.getGenreByMongoId(mongoBook.getGenre().getId()));

        Book savedBook = bookRepository.save(book);

        BookMigrate bookMigrate = new BookMigrate(savedBook.getId(), mongoBook.getId());
        bookIdMigrateRepository.save(bookMigrate);
        return savedBook;
    }

    public Book getBookByMongoId(String mongoId) {
        BookMigrate bookMigrate = bookIdMigrateRepository.findByMongoId(mongoId);
        return bookRepository.findById(bookMigrate.getId());
    }
}
