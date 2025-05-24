package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public Optional<Book> findById(long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        List<Book> results = namedParameterJdbcOperations.query(
                "SELECT b.id, " +
                        "b.title, " +
                        "au.id AS author_id, " +
                        "au.full_name AS author_name, " +
                        "ge.id AS genre_id, " +
                        "ge.name AS genre_name " +
                        "FROM books b " +
                        "INNER JOIN authors au ON b.author_id = au.id " +
                        "INNER JOIN genres ge ON b.genre_id = ge.id " +
                        "WHERE b.id = :id",
                params, new BookRowMapper()
        );

        return results.stream().findFirst();
    }

    @Override
    public List<Book> findAll() {
        return namedParameterJdbcOperations.query(
                "SELECT b.id, " +
                        "b.title, " +
                        "au.id AS author_id, " +
                        "au.full_name AS author_name, " +
                        "ge.id AS genre_id, " +
                        "ge.name AS genre_name " +
                        "FROM books b " +
                        "INNER JOIN authors au ON b.author_id = au.id " +
                        "INNER JOIN genres ge ON b.genre_id = ge.id ",
                new BookRowMapper()
        );
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        namedParameterJdbcOperations.update(
                "DELETE FROM books WHERE id = :id",
                params
        );
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("author_id", book.getAuthor().getId());
        params.addValue("genre_id", book.getGenre().getId());
        namedParameterJdbcOperations.update(
                "INSERT INTO books (title, author_id, genre_id) VALUES (:title, :author_id, :genre_id)",
                params,
                keyHolder,
                new String[]{"id"}
        );

        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        return book;
    }

    private Book update(Book book) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", book.getId());
        params.addValue("title", book.getTitle());
        params.addValue("author_id", book.getAuthor().getId());
        params.addValue("genre_id", book.getGenre().getId());

        int result = namedParameterJdbcOperations.update(
                "UPDATE books SET title = :title, author_id = :author_id, genre_id = :genre_id WHERE id = :id",
                params
        );
        if (result == 0) {
            throw new EntityNotFoundException("Book with id " + book.getId() + " not found");
        }
        return book;
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String title = rs.getString("title");

            long authorId = rs.getLong("author_id");
            String authorName = rs.getString("author_name");
            Author author = new Author(authorId, authorName);

            long genreId = rs.getLong("genre_id");
            String genreName = rs.getString("genre_name");
            Genre genre = new Genre(genreId, genreName);

            return new Book(id, title, author, genre);
        }
    }
}
