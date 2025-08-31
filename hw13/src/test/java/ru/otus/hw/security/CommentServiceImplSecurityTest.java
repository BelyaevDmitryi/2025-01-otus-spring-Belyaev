package ru.otus.hw.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.mapper.CommentMapper;
import ru.otus.hw.services.AclServiceWrapperServiceImpl;
import ru.otus.hw.services.CommentServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

@DisplayName("Безопасность. Сервис комментариев ")
@DataJpaTest
@Import({CommentServiceImpl.class, CommentMapper.class,
        AclServiceWrapperServiceImpl.class, AclConfig.class})
public class CommentServiceImplSecurityTest {

    @Autowired
    private CommentServiceImpl commentService;

    @Autowired
    private AclServiceWrapperServiceImpl aclServiceWrapperService;

    @DisplayName("для user и книги [1] отдаст список комментариев")
    @WithMockUser(username = "user", authorities = {})
    @Test()
    void shouldSelectCommentByBookTest1() {
        assertThat(commentService.findByBookId(1)).isNotNull().isNotEmpty().hasSize(3);
    }

    @DisplayName("для user и книги [2] отдаст пустой список комментариев")
    @WithMockUser(username = "user", authorities = {})
    @Test()
    void shouldSelectCommentByBookTest2() {
        assertThat(commentService.findByBookId(2)).isNotNull().isEmpty();
    }

    @DisplayName("для lib и книги [1] отдаст список комментариев")
    @WithMockUser(username = "lib", authorities = {"ROLE_BOOK_EDITOR", "ROLE_COMMENT_EDITOR"})
    @Test()
    void shouldSelectCommentByBookTest3() {
        assertThat(commentService.findByBookId(1)).isNotNull().isNotEmpty().hasSize(3);
    }

    @DisplayName("для lib и книги [2] отдаст список комментариев")
    @WithMockUser(username = "lib", authorities = {"ROLE_BOOK_EDITOR", "ROLE_COMMENT_EDITOR"})
    @Test()
    void shouldSelectCommentByBookTest4() {
        assertThat(commentService.findByBookId(2)).isNotNull().isNotEmpty().hasSize(2);
    }

    @DisplayName("для user и книги [1] успешно обновится комментарий, который раньше делал user")
    @WithMockUser(username = "user", authorities = {})
    @Test()
    void shouldUpdateCommentTest1() {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(1);
        commentDto.setText("text");
        commentDto.setBook(new BookDto());
        commentDto.getBook().setId(1);

        assertThatCode(() -> commentService.update(commentDto)).doesNotThrowAnyExceptionExcept();
    }

    @DisplayName("для user и книги [2] при обновлении комментария будет ошибка 403")
    @WithMockUser(username = "user", authorities = {})
    @Test()
    void shouldUpdateCommentTest2() {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(4);
        commentDto.setText("text");
        commentDto.setBook(new BookDto());
        commentDto.getBook().setId(2);

        Assertions.assertThrows(AuthorizationDeniedException.class, () -> commentService.update(commentDto));
    }

    @DisplayName("для lib и книги [1] успешно обновится комментарий, который раньше делал user")
    @WithMockUser(username = "lib", authorities = {"ROLE_BOOK_EDITOR", "ROLE_COMMENT_EDITOR"})
    @Test()
    void shouldUpdateCommentTest3() {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(1);
        commentDto.setText("text");
        commentDto.setBook(new BookDto());
        commentDto.getBook().setId(1);

        assertThatCode(() -> commentService.update(commentDto)).doesNotThrowAnyExceptionExcept();
    }

    @DisplayName("для user и книги [1] успешно удалить комментарий, который раньше делал user")
    @WithMockUser(username = "user", authorities = {})
    @Test()
    void shouldDeleteCommentTest1() {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(1);
        commentDto.setText("text");
        commentDto.setBook(new BookDto());
        commentDto.getBook().setId(1);

        assertThatCode(() -> commentService.delete(commentDto)).doesNotThrowAnyExceptionExcept();
    }

    @DisplayName("для user и книги [2] при удалении комментария будет ошибка 403")
    @WithMockUser(username = "user", authorities = {})
    @Test()
    void shouldDeleteCommentTest2() {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(4);
        commentDto.setText("text");
        commentDto.setBook(new BookDto());
        commentDto.getBook().setId(2);

        Assertions.assertThrows(AuthorizationDeniedException.class, () -> commentService.delete(commentDto));
    }

    @DisplayName("для lib и книги [1] при удалении комментария будет ошибка 403")
    @WithMockUser(username = "lib", authorities = {"ROLE_BOOK_EDITOR", "ROLE_COMMENT_EDITOR"})
    @Test()
    void shouldDeleteCommentTest3() {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(1);
        commentDto.setText("text");
        commentDto.setBook(new BookDto());
        commentDto.getBook().setId(1);

        Assertions.assertThrows(AuthorizationDeniedException.class, () -> commentService.delete(commentDto));
    }
}
