package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Author;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с авторами ")
@DataJpaTest
class AuthorRepositoryTest {

    private static final long FIRST_ID = 1;

    @Autowired
    private AuthorRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @DisplayName("должен загружать список всех авторов")
    @Test
    void shouldReturnCorrectAuthorsList() {
        var actualAuthors = repository.findAll();
        assertThat(actualAuthors).isNotNull()
                .allMatch(author -> author.getFullName() != null && !author.getFullName().isEmpty());
    }

    @DisplayName("должен загружать автора по id")
    @Test
    void shouldReturnCorrectAuthorById() {
        var actualAuthor = repository.findById(FIRST_ID);
        var expectedAuthor = entityManager.find(Author.class, FIRST_ID);
        assertThat(actualAuthor).isPresent()
                .get()
                .isEqualTo(expectedAuthor);
    }
}
