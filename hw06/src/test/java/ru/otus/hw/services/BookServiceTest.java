package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.models.Book;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

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
        var expectedBook = bookService.insert("new_book_title", 1, 1);
        assertThat(expectedBook).isNotNull();
        assertThatCode(() -> bookConverter.bookToString(expectedBook)).doesNotThrowAnyException();
    }

    @DisplayName("должен обновить книгу")
    @Test
    void shouldUpdateBook() {
        var expectedBook = bookService.update(1,"update_book_title", 1, 1);
        assertThat(expectedBook).isNotNull();
        assertThatCode(() -> bookConverter.bookToString(expectedBook)).doesNotThrowAnyException();
    }

    @DisplayName("должен удалить книгу")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDeleteBook() {
        assertThatCode(() -> bookService.deleteById(1)).doesNotThrowAnyException();
    }
}
