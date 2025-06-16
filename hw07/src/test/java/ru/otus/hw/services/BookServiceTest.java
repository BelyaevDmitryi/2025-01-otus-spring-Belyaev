package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional(propagation = Propagation.NEVER)
class BookServiceTest {

    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private BookConverter bookConverter;

    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        Book expectedBook = bookService.findById(1);
        assertThat(expectedBook).isNotNull();
        assertThat(expectedBook.getId()).isEqualTo(1);
        assertThat(expectedBook.getTitle()).isEqualTo("BookTitle_1");
    }

    @DisplayName("должен загружать все книги")
    @Test
    void shouldReturnCorrectAllBook() {
        var expectedBooks = bookService.findAll();
        assertThat(expectedBooks).isNotNull().isNotEmpty();
        assertThatCode(() -> expectedBooks.forEach(book -> bookConverter.bookToString(book)))
                .doesNotThrowAnyException();
    }

    @DisplayName("должен добавить книгу")
    @Test
    void shouldInsertBook() {
        var author = new Author(1, "Test Author");
        var genre = new Genre(1, "Test Genre");

        var bookToInsert = new Book(0, "new_book_title", author, genre);
        var expectedBook = bookService.insert(bookToInsert);

        assertThat(expectedBook).isNotNull();
        assertThat(expectedBook.getId()).isPositive();
        assertThat(expectedBook.getTitle()).isEqualTo("new_book_title");
        assertThat(expectedBook.getAuthor().getId()).isEqualTo(1);
        assertThat(expectedBook.getGenre().getId()).isEqualTo(1);

        assertThatCode(() -> bookConverter.bookToString(expectedBook)).doesNotThrowAnyException();
        assertThat(bookConverter.bookToString(expectedBook)).contains("new_book_title");
    }

    @DisplayName("должен обновить книгу")
    @Test
    void shouldUpdateBook() {
        var author = new Author(1, "Test Author");
        var genre = new Genre(1, "Test Genre");

        var bookToUpdate = new Book(1, "update_book_title", author, genre);
        var expectedBook = bookService.update(bookToUpdate);
        assertThat(expectedBook).isNotNull();
        assertThatCode(() -> bookConverter.bookToString(expectedBook)).doesNotThrowAnyException();
    }

    @DisplayName("должен удалить книгу")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDeleteBook() {
        assertThatCode(() -> bookService.deleteById(1)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("должен выбросить EntityNotFoundException при добавлении книги с несуществующим автором")
    void shouldThrowEntityNotFoundExceptionWhenInsertWithNonExistingAuthor() {
        var invalidAuthor = new Author(999L, null);
        var validGenre = new Genre(1L, null);
        var bookToInsert = new Book(0, "invalid_book", invalidAuthor, validGenre);

        assertThatThrownBy(() -> bookService.insert(bookToInsert))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Author with id 999 not found");
    }

    @Test
    @DisplayName("должен выбросить EntityNotFoundException при добавлении книги с несуществующим жанром")
    void shouldThrowEntityNotFoundExceptionWhenInsertWithNonExistingGenre() {
        var validAuthor = new Author(1L, null);
        var invalidGenre = new Genre(999L, null);
        var bookToInsert = new Book(0, "invalid_book", validAuthor, invalidGenre);

        assertThatThrownBy(() -> bookService.insert(bookToInsert))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Genre with id 999 not found");
    }

    @Test
    @DisplayName("должен выбросить EntityNotFoundException при обновлении книги с несуществующим автором")
    void shouldThrowEntityNotFoundExceptionWhenUpdateWithNonExistingAuthor() {
        var invalidAuthor = new Author(999L, null);
        var validGenre = new Genre(1L, null);
        var bookToUpdate = new Book(1L, "invalid_update", invalidAuthor, validGenre);

        assertThatThrownBy(() -> bookService.update(bookToUpdate))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Author with id 999 not found");
    }

    @Test
    @DisplayName("должен выбросить EntityNotFoundException при обновлении книги с несуществующим жанром")
    void shouldThrowEntityNotFoundExceptionWhenUpdateWithNonExistingGenre() {
        var validAuthor = new Author(1L, null);
        var invalidGenre = new Genre(999L, null);
        var bookToUpdate = new Book(1L, "invalid_update", validAuthor, invalidGenre);

        assertThatThrownBy(() -> bookService.update(bookToUpdate))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Genre with id 999 not found");
    }
}
