package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с жанрами ")
@DataJpaTest
class GenreRepositoryTest {

    private static final long FIRST_ID = 1;

    @Autowired
    private GenreRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @DisplayName("должен загружать список всех жанров")
    @Test
    void shouldReturnCorrectGenresList() {
        var actualGenres = repository.findAll();

        assertThat(actualGenres).isNotNull()
                .allMatch(genre -> genre.getName() != null && !genre.getName().isEmpty());
    }

    @DisplayName("должен загружать жанр по id")
    @Test
    void shouldReturnCorrectGenreById() {
        var actualGenre = repository.findById(FIRST_ID);
        var expectedGenre = entityManager.find(Genre.class, FIRST_ID);
        assertThat(actualGenre).isPresent()
                .get()
                .isEqualTo(expectedGenre);
    }
}
