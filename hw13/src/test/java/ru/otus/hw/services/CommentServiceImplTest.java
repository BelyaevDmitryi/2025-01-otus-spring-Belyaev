package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentSaveDto;
import ru.otus.hw.mapper.CommentMapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

@DisplayName("Сервис комментариев ")
@DataJpaTest
@Import({CommentServiceImpl.class, CommentMapper.class})
@Transactional(propagation = Propagation.NEVER)
public class CommentServiceImplTest {

    @Autowired
    private CommentServiceImpl commentService;

    @MockitoBean
    private AclServiceWrapperServiceImpl aclServiceWrapperService;

    @DisplayName("должен загружать комментарий по id")
    @Test
    void shouldReturnCorrectCommentById() {
        assertThatCode(() -> commentService.findById(1)).doesNotThrowAnyExceptionExcept();
    }

    @DisplayName("должен загружать комментарий для книги")
    @Test
    void shouldReturnCorrectCommentByBookId() {
        assertThatCode(() -> commentService.findByBookId(1)).doesNotThrowAnyExceptionExcept();
    }

    @DisplayName("должен добавить комментарий для книги")
    @Test
    void shouldCreateComment() {
        CommentSaveDto commentSaveDto = new CommentSaveDto(0, "new_comment", 1);
        assertThatCode(() -> commentService.create(commentSaveDto)).doesNotThrowAnyExceptionExcept();
    }

    @DisplayName("должен обновить комментарий для книги")
    @Test
    void shouldUpdateComment() {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(1);
        commentDto.setText("updated_comment_text");
        commentDto.setBook(new BookDto());
        commentDto.getBook().setId(1);

        assertThatCode(() -> commentService.update(commentDto)).doesNotThrowAnyExceptionExcept();
    }

    @DisplayName("должен удалить комментарий")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDeleteComment() {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(1);
        commentDto.setText("text");
        commentDto.setBook(new BookDto());
        commentDto.getBook().setId(1);

        assertThatCode(() -> commentService.delete(commentDto)).doesNotThrowAnyExceptionExcept();
    }
}
