package ru.otus.hw.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentSaveDto;
import ru.otus.hw.mapper.CommentMapper;
import ru.otus.hw.services.CommentService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@DisplayName("Контроллер для комментарив")
@WebMvcTest(CommentController.class)
@Import({CommentMapper.class})
class CommentControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CommentService commentService;

    @Autowired
    private CommentMapper commentMapper;

    @DisplayName("должен отдать страницу с комментариями для книги с моделью")
    @Test
    void shouldRenderCommentForBookPageWithModel() throws Exception {
        List<CommentDto> commentDtoList = createCommentDtoList();

        when(commentService.findByBookId(1)).thenReturn(commentDtoList);

        mvc.perform(get("/comment/book/1"))
                .andExpect(view().name("comments"))
                .andExpect(model().attribute("comments", commentDtoList))
                .andExpect(model().attribute("bookId", 1L));
    }

    @DisplayName("должен отдать страницу для редактирования комментария с моделью")
    @Test
    void shouldRenderEditCommentPageWithModel() throws Exception {
        List<CommentDto> commentDtoList = createCommentDtoList();
        CommentDto commentDto = commentDtoList.get(0);

        when(commentService.findById(1)).thenReturn(commentDto);

        mvc.perform(get("/comment/1"))
                .andExpect(view().name("comment_edit"))
                .andExpect(model().attribute("comment", commentMapper.toSaveDto(commentDto)));
    }

    @DisplayName("должен обновить комментарий и перенаправить на страницу с комментариями для книги")
    @Test
    void shouldSaveEditCommentAndRedirectCommentForBookPage() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(1);
        commentDto.setText("updated_comment_text");
        commentDto.setBook(new BookDto());
        commentDto.getBook().setId(1);

        CommentSaveDto commentSaveDto = new CommentSaveDto(1, "updated_comment_text", 1);

        when(commentService.update(commentSaveDto)).thenReturn(commentDto);

        mvc.perform(post("/comment")
                        .param("id", "1")
                        .param("bookId", "1")
                        .param("text", "updated_comment_text"))
                .andExpect(view().name("redirect:/comment/book/1"));

        verify(commentService, times(1)).update(commentSaveDto);
    }

    @DisplayName("должен удалить комментарий и перенаправить на страницу с комментариями для книги")
    @Test
    void shouldDeleteCommentAndRedirectCommentForBookPage() throws Exception {
        List<CommentDto> commentDtoList = createCommentDtoList();
        CommentDto commentDto = commentDtoList.get(0);

        when(commentService.findById(1)).thenReturn(commentDto);

        mvc.perform(post("/comment/1/delete"))
                .andExpect(view().name("redirect:/comment/book/1"));

        verify(commentService, times(1)).deleteById(1);
    }

    @DisplayName("должен отдать страницу для создания комментария с моделью")
    @Test
    void shouldRenderNewCommentPageWithModel() throws Exception {
        CommentSaveDto commentSaveDto = new CommentSaveDto();
        commentSaveDto.setBookId(1L);

        mvc.perform(get("/comment/book/1/create"))
                .andExpect(view().name("comment_edit"))
                .andExpect(model().attribute("comment", commentSaveDto));
    }

    @DisplayName("должен сохранить комментарий и перенаправить на страницу с комментариями для книги")
    @Test
    void shouldSaveNewCommentAndRedirectCommentForBookPage() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(1);
        commentDto.setText("new_comment");
        commentDto.setBook(new BookDto());
        commentDto.getBook().setId(1);

        CommentSaveDto commentSaveDto = new CommentSaveDto(0, "new_comment", 1);

        when(commentService.create(commentSaveDto)).thenReturn(commentDto);

        mvc.perform(post("/comment")
                        .param("id", "0")
                        .param("bookId", "1")
                        .param("text", "new_comment"))
                .andExpect(view().name("redirect:/comment/book/1"));

        verify(commentService, times(1)).create(commentSaveDto);
    }

    @DisplayName("должен сделать redirect на страницу редактирования из-за пустого комментария")
    @Test
    void shouldNotSaveBookWithEmptyTitleAndRedirectEditPage() throws Exception {
        CommentSaveDto commentSaveDto = new CommentSaveDto(1, "", 1);

        mvc.perform(post("/comment"))
                .andExpect(model().attributeHasFieldErrorCode("comment", "text", "NotBlank"))
                .andExpect(view().name("comment_edit"));

        verify(commentService, times(0)).update(commentSaveDto);
    }

    private List<CommentDto> createCommentDtoList() {
        List<CommentDto> commentDtoList = new ArrayList<>();

        for (int i = 1; i < 5; i++) {
            CommentDto commentDto = new CommentDto();
            commentDto.setText("comment_text_" + i);
            commentDto.setId(i);
            commentDto.setBook(new BookDto());
            commentDto.getBook().setId(1);
            commentDtoList.add(commentDto);
        }

        return commentDtoList;
    }
}
