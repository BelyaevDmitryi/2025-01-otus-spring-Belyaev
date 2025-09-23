package ru.otus.hw.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.mongo.MongoAuthor;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.models.mongo.MongoComment;
import ru.otus.hw.models.mongo.MongoGenre;
import ru.otus.hw.repositories.mongo.MongoAuthorRepository;
import ru.otus.hw.repositories.mongo.MongoBookRepository;
import ru.otus.hw.repositories.mongo.MongoCommentRepository;
import ru.otus.hw.repositories.mongo.MongoGenreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@ChangeLog(order = "001")
public class InitMongoDBDataChangeLog {

    private static final int AUTHOR_SIZE = 15;

    private static final int GENRE_SIZE = 5;

    private static final int COMMENT_MAX_SIZE = 10;

    private List<MongoGenre> genreList;

    private List<MongoAuthor> authorList;

    private List<MongoBook> bookList;

    @ChangeSet(order = "000", id = "dropDB", author = "mongock", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "initGenre", author = "mongock", runAlways = true)
    public void initGenre(MongoGenreRepository genreRepository) {
        genreList = new ArrayList<MongoGenre>();

        for (int num = 0; num < GENRE_SIZE; num++) {
            MongoGenre genre = genreRepository.save(new MongoGenre("Mongo_Genre_" + num));
            genreList.add(genre);
        }
    }

    @ChangeSet(order = "002", id = "initAuthor", author = "mongock", runAlways = true)
    public void initAuthor(MongoAuthorRepository authorRepository) {
        authorList = new ArrayList<MongoAuthor>();

        for (int num = 0; num < AUTHOR_SIZE; num++) {
            MongoAuthor author = authorRepository.save(new MongoAuthor("Mongo_Author_" + num));
            authorList.add(author);
        }
    }

    @ChangeSet(order = "003", id = "initBook", author = "mongock", runAlways = true)
    public void initBook(MongoBookRepository bookRepository) {
        bookList = new ArrayList<MongoBook>();

        for (int num = 0; num < 10; num++) {
            var author = authorList.get(new Random().nextInt(0, AUTHOR_SIZE));
            var genre = genreList.get(new Random().nextInt(0, GENRE_SIZE));

            MongoBook book = bookRepository.save(new MongoBook("Mongo_Title_" + num,
                    author, genre));
            bookList.add(book);
        }
    }

    @ChangeSet(order = "004", id = "initComment", author = "mongock", runAlways = true)
    public void initComment(MongoCommentRepository commentRepository) {
        for (MongoBook book : bookList) {
            int size = new Random().nextInt(0, COMMENT_MAX_SIZE);

            for (int num = 0; num < size; num++) {
                commentRepository.save(new MongoComment("Mongo_comment_" + num + "_for_" + book.getTitle(), book));
            }
        }
    }
}
