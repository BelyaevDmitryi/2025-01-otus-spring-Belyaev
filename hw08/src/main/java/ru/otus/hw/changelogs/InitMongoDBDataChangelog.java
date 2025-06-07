package ru.otus.hw.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
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

@ChangeLog(order = "001")
public class InitMongoDBDataChangelog {


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

        for (int num = 0; num < 3; num++) {
            Genre genre = genreRepository.save(new Genre("Genre_" + num));
            genreList.add(genre);
        }
    }

    @ChangeSet(order = "002", id = "initAuthor", author = "mongock", runAlways = true)
    public void initAuthor(AuthorRepository authorRepository) {
        authorList = new ArrayList<>();

        for (int num = 0; num < 3; num++) {
            Author author = authorRepository.save(new Author("Author_" + num));
            authorList.add(author);
        }
    }

    @ChangeSet(order = "003", id = "initBook", author = "mongock", runAlways = true)
    public void initBook(BookRepository bookRepository) {
        bookList = new ArrayList<>();

        for (int num = 0; num < 3; num++) {
            Book book = bookRepository.save(new Book("Title_" + num, authorList.get(num), genreList.get(num)));
            bookList.add(book);
        }
    }

    @ChangeSet(order = "004", id = "initComment", author = "mongock", runAlways = true)
    public void initComment(CommentRepository commentRepository) {
        for (Book book : bookList) {
            for (int num = 0; num < 3; num++) {
                commentRepository.save(new Comment("comment_" + num + "_for_" + book.getTitle(), book));
            }
        }
    }
}
