package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с комментариями ")
@DataJpaTest
@Import({JpaCommentRepository.class})
class JpaCommentRepositoryTest {

    private static final long FIRST_ID = 1;

    @Autowired
    private JpaCommentRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @DisplayName("должен загружать комментарий по id")
    @Test
    void shouldReturnCorrectCommentById() {
        var actualComment = repository.findById(FIRST_ID);
        var expectedComment = entityManager.find(Comment.class, FIRST_ID);
        assertThat(actualComment).isPresent()
                .get()
                .isEqualTo(expectedComment);
    }

    @DisplayName("должен загружать комментарий по id книги")
    @Test
    void shouldReturnCorrectCommentByBookId() {
        var actualComment = repository.findByBookId(FIRST_ID);

        assertThat(actualComment).isNotNull()
                .allMatch(comment -> comment.getText() != null && !comment.getText().isEmpty());
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void shouldSaveNewComment() {
        var book = entityManager.find(Book.class, FIRST_ID);

        var expectedComment = new Comment(0, "Text_1_12345", book);
        var actualComment = repository.save(expectedComment);

        assertThat(actualComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(entityManager.find(Comment.class, actualComment.getId()))
                .isNotNull()
                .isEqualTo(actualComment);
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    void shouldSaveUpdatedComment() {
        var book = entityManager.find(Book.class, FIRST_ID);
        var expectedComment = new Comment(FIRST_ID, "Text_1_12345", book);
        assertThat(entityManager.find(Comment.class, expectedComment.getId())).isNotNull()
                .isNotEqualTo(expectedComment);

        var actualComment = repository.save(expectedComment);
        assertThat(actualComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(entityManager.find(Comment.class, actualComment.getId())).isEqualTo(actualComment);
    }

    @DisplayName("должен удалять комментарий по id")
    @Test
    void shouldDeleteComment() {
        assertThat(entityManager.find(Comment.class, 1)).isNotNull();
        repository.deleteById(FIRST_ID);
        assertThat(entityManager.find(Comment.class, 1)).isNull();
    }
}
