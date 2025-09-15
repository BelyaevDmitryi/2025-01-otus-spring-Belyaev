package ru.otus.hw.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@ChangeLog(order = "001")
public class InitMongoDBDataChangeLog {

    private static final int AUTHOR_SIZE = 10;

    private static final int GENRE_SIZE = 5;

    private static final int COMMENT_MAX_SIZE = 10;

    private List<Genre> genreList;

    private List<Author> authorList;

    private List<Book> bookList;

    @ChangeSet(order = "000", id = "dropDB", author = "mongock", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "initGenre", author = "mongock", runAlways = true)
    public void initGenre(GenreRepository genreRepository) {
        genreList = new ArrayList<>();

        for (int num = 0; num < GENRE_SIZE; num++) {
            Mono<Genre> genre = genreRepository.save(new Genre("Genre_" + num));
            genreList.add(genre.block());
        }
    }

    @ChangeSet(order = "002", id = "initAuthor", author = "mongock", runAlways = true)
    public void initAuthor(AuthorRepository authorRepository) {
        authorList = new ArrayList<>();

        for (int num = 0; num < AUTHOR_SIZE; num++) {
            Mono<Author> author = authorRepository.save(new Author("Author_" + num));
            authorList.add(author.block());
        }
    }

    @ChangeSet(order = "003", id = "initBook", author = "mongock", runAlways = true)
    public void initBook(BookRepository bookRepository) {
        bookList = new ArrayList<>();

        for (int num = 0; num < 10; num++) {
            var author = authorList.get(new Random().nextInt(0, AUTHOR_SIZE));
            var genre = genreList.get(new Random().nextInt(0, GENRE_SIZE));

            Mono<Book> book = bookRepository.save(new Book("Title_" + num, author, genre));
            bookList.add(book.block());
        }
    }

    @ChangeSet(order = "004", id = "initComment", author = "mongock", runAlways = true)
    public void initComment(CommentRepository commentRepository) {
        for (Book book : bookList) {
            int size = new Random().nextInt(1, COMMENT_MAX_SIZE);

            for (int num = 0; num < size; num++) {
                commentRepository.save(new Comment("comment_" + num + "_for_" + book.getTitle(), book))
                        .block();
            }
        }
    }
}
