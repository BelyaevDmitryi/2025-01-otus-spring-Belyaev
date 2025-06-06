package ru.otus.hw.repositories;

import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с книгами ")
@DataJpaTest
@Import({JpaBookRepository.class})
class JpaBookRepositoryTest {

    private static final String FIELD_ID = "id";

    private static final long FIRST_BOOK_ID = 1L;
    private static final long SECOND_BOOK_ID = 2L;

    @Autowired
    private JpaBookRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @DisplayName("должен загружать список всех книг с полной информацией о них")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = repository.findAll();
        assertThat(actualBooks).isNotNull()
                .allMatch(book -> !book.getTitle().isEmpty())
                .allMatch(book -> book.getGenre() != null && !book.getGenre().getName().isEmpty())
                .allMatch(book -> book.getAuthor() != null && !book.getAuthor().getFullName().isEmpty());
    }

    @DisplayName(" должен загружать информацию о нужной книге")
    @Test
    void shouldFindExpectedBookById() {
        var actualBook = repository.findById(FIRST_BOOK_ID);
        assertThat(actualBook).isNotNull();
        var expectedBook = entityManager.find(Book.class, FIRST_BOOK_ID);
        assertThat(actualBook).isPresent()
                .get()
                .isEqualTo(expectedBook);
    }

    @DisplayName(" должен сохранить информацию о нужной книге")
    @Test
    void shouldSaveCorrectBook() {
        var author = entityManager.find(Author.class, FIRST_BOOK_ID);
        var genre = entityManager.find(Genre.class, FIRST_BOOK_ID);

        var expectedBook = new Book(0, "BookTitle_123", author, genre);
        var actualBook = repository.save(expectedBook);

        assertThat(actualBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison(
                        RecursiveComparisonConfiguration.builder()
                                .withIgnoredFields(FIELD_ID).build())
                .isEqualTo(expectedBook);
        assertThat(entityManager.find(Book.class, actualBook.getId())).isNotNull().isEqualTo(actualBook);
    }


    @DisplayName(" должен обновлять имя книги в БД")
    @Test
    void shouldUpdateBookName() {
        var author = entityManager.find(Author.class, SECOND_BOOK_ID);
        var genre = entityManager.find(Genre.class, SECOND_BOOK_ID);

        var expectedBook = new Book(FIRST_BOOK_ID, "BookTitle_123", author, genre);

        assertThat(entityManager.find(Book.class, expectedBook.getId())).isNotNull()
                .isNotEqualTo(expectedBook);
        var actualBook = repository.save(expectedBook);
        assertThat(actualBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);
        assertThat(entityManager.find(Book.class, actualBook.getId())).isEqualTo(actualBook);
    }

    @DisplayName("должен удалять книгу из БД по id")
    @Test
    void shouldDeleteBookFromDbById() {
        assertThat(entityManager.find(Book.class, FIRST_BOOK_ID)).isNotNull();
        repository.deleteById(1);
        assertThat(entityManager.find(Book.class, FIRST_BOOK_ID)).isNull();
    }
}
