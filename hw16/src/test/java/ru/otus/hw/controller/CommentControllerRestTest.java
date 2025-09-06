package ru.otus.hw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentSaveDto;
import ru.otus.hw.dto.ErrorDto;
import ru.otus.hw.services.CommentService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("REST Контроллер для комментарив")
@WebMvcTest(CommentControllerRest.class)
class CommentControllerRestTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private CommentService commentService;

    @DisplayName("должен сохранить комментарий")
    @Test
    void shouldCreateComment() throws Exception {
        BookDto bookDto = new BookDto();
        bookDto.setId(1);
        CommentDto commentDto = new CommentDto(1, "new_comment", bookDto);

        CommentSaveDto commentCreateDto = new CommentSaveDto(1, "new_comment", 1L);

        when(commentService.create(commentCreateDto)).thenReturn(commentDto);

        String expectedResult = mapper.writeValueAsString(commentCreateDto);

        mvc.perform(post("/api/comment")
                        .contentType(APPLICATION_JSON)
                        .content(expectedResult))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(commentDto)));
    }

    @DisplayName("должен при создании комментария вернуть ошибку, если комментарий пустой")
    @Test
    void shouldReturnErrorEmptyCommentForCreateComment() throws Exception {
        CommentSaveDto commentCreateDto = new CommentSaveDto(1, "", 1L);
        String expectedResult = mapper.writeValueAsString(commentCreateDto);

        ErrorDto errorDto = new ErrorDto(400, "The comment should not be empty.");

        mvc.perform(post("/api/comment")
                        .contentType(APPLICATION_JSON)
                        .content(expectedResult))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(mapper.writeValueAsString(errorDto)));
    }

    @DisplayName("должен обновить комментарий")
    @Test
    void shouldUpdateComment() throws Exception {
        BookDto bookDto = new BookDto();
        bookDto.setId(1);
        CommentDto commentDto = new CommentDto(1, "update_comment", bookDto);

        CommentSaveDto commentUpdateDto = new CommentSaveDto(1, "update_comment", 1);

        when(commentService.update(commentUpdateDto)).thenReturn(commentDto);

        String expectedResult = mapper.writeValueAsString(commentUpdateDto);

        mvc.perform(put("/api/comment")
                        .contentType(APPLICATION_JSON)
                        .content(expectedResult))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(commentDto)));
    }

    @DisplayName("должен удалить комментарий")
    @Test
    void shouldDeleteComment() throws Exception {
        mvc.perform(delete("/api/comment/1"))
                .andExpect(status().isNoContent());

        verify(commentService, times(1)).deleteById(1);
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
