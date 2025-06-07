package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.exceptions.EntityNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DataMongoTest
@Import({BookServiceImpl.class, CommentServiceImpl.class, CommentConverter.class})
class CommentServiceImplTest {

    private final String BOOK_ID = "1";
    private final String COMMENT_ID = "1";

    @Autowired
    private CommentServiceImpl commentService;

    @Autowired
    private CommentConverter commentConverter;

    @Autowired
    private BookServiceImpl bookService;

    @DisplayName("загружает комментарий по id")
    @Test
    void loadCommentById() {
        assertThatCode(() -> commentService.findById(COMMENT_ID)).doesNotThrowAnyExceptionExcept(EntityNotFoundException.class);

        var bookList = bookService.findAll();
        assertThat(bookList).isNotNull().isNotEmpty();

        var commentList = commentService.findByBookId(bookList.get(0).getId());
        assertThat(commentList).isNotNull().isNotEmpty();

        var expectedComment = commentService.findById(commentList.get(0).getId());
        assertThat(expectedComment).isPresent();
        assertThatCode(() -> commentConverter.commentToString(expectedComment.get())).doesNotThrowAnyExceptionExcept();
    }

    @DisplayName("загружает комментарий для книги")
    @Test
    void loadCommentByBookId() {
        assertThatCode(() -> commentService.findByBookId(COMMENT_ID))
                .doesNotThrowAnyExceptionExcept(EntityNotFoundException.class);

        var bookList = bookService.findAll();
        assertThat(bookList).isNotNull().isNotEmpty();

        var expectedComments = commentService.findByBookId(bookList.get(0).getId());
        assertThat(expectedComments).isNotNull().isNotEmpty();
        assertThatCode(() -> expectedComments.forEach(comment -> commentConverter.commentToString(comment)))
                .doesNotThrowAnyExceptionExcept();
    }

    @DisplayName("добавляет комментарий для книги")
    @Test
    void insertComment() {
        assertThatCode(() -> commentService.insert(BOOK_ID, "new_comment_text"))
                .doesNotThrowAnyExceptionExcept(EntityNotFoundException.class);

        var bookList = bookService.findAll();
        assertThat(bookList).isNotNull().isNotEmpty();

        var expectedComment = commentService.insert(bookList.get(0).getId(), "new_comment_text");
        assertThat(expectedComment).isNotNull();
        assertThatCode(() -> commentConverter.commentToString(expectedComment)).doesNotThrowAnyExceptionExcept();
    }

    @DisplayName("обновить комментарий для книги")
    @Test
    void updateComment() {
        assertThatCode(() ->  commentService.update(COMMENT_ID, "updated_comment_text"))
                .doesNotThrowAnyExceptionExcept(EntityNotFoundException.class);

        var bookList = bookService.findAll();
        assertThat(bookList).isNotNull().isNotEmpty();

        var commentList = commentService.findByBookId(bookList.get(0).getId());
        assertThat(commentList).isNotNull().isNotEmpty();

        var expectedComment = commentService.update(commentList.get(0).getId(), "updated_comment_text");
        assertThat(expectedComment).isNotNull();
        assertThatCode(() -> commentConverter.commentToString(expectedComment)).doesNotThrowAnyExceptionExcept();
    }

    @DisplayName("должен удалить комментарий")
    @Test
    void deleteComment() {
        assertThatCode(() -> commentService.deleteById(COMMENT_ID)).doesNotThrowAnyExceptionExcept();
    }
}
