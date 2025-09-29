package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookSaveDto;
import ru.otus.hw.mapper.AuthorMapper;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.mapper.GenreMapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

@DataJpaTest
@Import({BookServiceImpl.class, BookMapper.class, AuthorMapper.class, GenreMapper.class})
@Transactional(propagation = Propagation.NEVER)
class BookServiceImplTest {

    @Autowired
    private BookServiceImpl bookService;

    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        assertThatCode(() -> bookService.findById(1)).doesNotThrowAnyExceptionExcept();
    }

    @DisplayName("должен загружать все книги")
    @Test
    void shouldReturnCorrectAllBook() {
        assertThatCode(() -> bookService.findAll()).doesNotThrowAnyExceptionExcept();
    }

    @DisplayName("должен добавить книгу")
    @Test
    void shouldCreateBook() {
        BookSaveDto bookSaveDto = new BookSaveDto(0, "new_book_title", 1, 1);
        assertThatCode(() -> bookService.create(bookSaveDto)).doesNotThrowAnyExceptionExcept();
    }

    @DisplayName("должен обновить книгу")
    @Test
    void shouldUpdateBook() {
        BookSaveDto bookSaveDto = new BookSaveDto(1, "update_book_title", 1, 1);
        assertThatCode(() -> bookService.update(bookSaveDto)).doesNotThrowAnyExceptionExcept();
    }

    @DisplayName("должен удалить книгу")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDeleteBook() {
        assertThatCode(() -> bookService.deleteById(1)).doesNotThrowAnyExceptionExcept();
    }
}
