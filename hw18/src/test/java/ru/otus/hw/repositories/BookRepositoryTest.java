package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с книгами ")
@DataJpaTest
class BookRepositoryTest {

    private static final long FIRST_ID = 1;

    private static final long SECOND_ID = 2;

    @Autowired
    private BookRepository repositoryJpa;

    @Autowired
    private TestEntityManager entityManager;

    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        var actualBook = repositoryJpa.findById(FIRST_ID);
        var expectedBook = entityManager.find(Book.class, FIRST_ID);
        assertThat(actualBook).isPresent()
                .get()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = repositoryJpa.findAll();

        assertThat(actualBooks).isNotNull()
                .allMatch(book -> !book.getTitle().isEmpty())
                .allMatch(book -> book.getGenre() != null && !book.getGenre().getName().isEmpty())
                .allMatch(book -> book.getAuthor() != null && !book.getAuthor().getFullName().isEmpty());

        actualBooks.forEach(System.out::println);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var author = entityManager.find(Author.class, FIRST_ID);
        var genre = entityManager.find(Genre.class, FIRST_ID);

        var expectedBook = new Book(0, "BookTitle_10500", author, genre);
        var returnedBook = repositoryJpa.save(expectedBook);

        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(entityManager.find(Book.class, returnedBook.getId())).isNotNull().isEqualTo(returnedBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var author = entityManager.find(Author.class, SECOND_ID);
        var genre = entityManager.find(Genre.class, SECOND_ID);

        var expectedBook = new Book(1L, "BookTitle_10500", author, genre);

        assertThat(entityManager.find(Book.class, expectedBook.getId())).isNotNull()
                .isNotEqualTo(expectedBook);

        var returnedBook = repositoryJpa.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(entityManager.find(Book.class, returnedBook.getId())).isEqualTo(returnedBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        assertThat(entityManager.find(Book.class, FIRST_ID)).isNotNull();
        repositoryJpa.deleteById(1L);
        assertThat(entityManager.find(Book.class, FIRST_ID)).isNull();
    }

}
