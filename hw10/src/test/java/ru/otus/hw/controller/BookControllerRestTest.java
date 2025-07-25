package ru.otus.hw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookSaveDto;
import ru.otus.hw.dto.ErrorDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.services.BookService;

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

@DisplayName("REST Контроллер для книг")
@WebMvcTest(BookControllerRest.class)
class BookControllerRestTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private BookService bookService;

    @DisplayName("должен отдать список книг")
    @Test
    void shouldReturnBookList() throws Exception {
        List<BookDto> bookDtoList = createBookList();

        when(bookService.findAll()).thenReturn(bookDtoList);

        mvc.perform(get("/api/book"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookDtoList)));
    }

    @DisplayName("должен данные для одной книги")
    @Test
    void shouldReturnBookById() throws Exception {
        List<BookDto> bookDtoList = createBookList();
        BookDto bookDto = bookDtoList.get(0);

        when(bookService.findById(1)).thenReturn(bookDto);

        mvc.perform(get("/api/book/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookDto)));
    }

    @DisplayName("должен возвратить ошибку, если книга не найдена")
    @Test
    void shouldReturnErrorForBookById() throws Exception {
        when(bookService.findById(1)).thenThrow(new NotFoundException("error"));

        ErrorDto errorDto = new ErrorDto(404, "error");

        mvc.perform(get("/api/book/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().json(mapper.writeValueAsString(errorDto)));
    }

    @DisplayName("должен создать книгу")
    @Test
    void shouldCreateBook() throws Exception {
        BookSaveDto bookCreateDto = new BookSaveDto(1, "new_book_title", 1L, 1L);

        BookDto bookDto = new BookDto(10L, "new_book_title",
                new AuthorDto(1L, "Author"), new GenreDto(1L, "Genre"));

        when(bookService.create(bookCreateDto)).thenReturn(bookDto);

        String expectedResult = mapper.writeValueAsString(bookCreateDto);

        mvc.perform(post("/api/book")
                        .contentType(APPLICATION_JSON)
                        .content(expectedResult))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(bookDto)));
    }

    @DisplayName("должен при создании книги вернуть ошибку, если заголовок пустой")
    @Test
    void shouldReturnErrorEmptyTitleForCreateBook() throws Exception {
        BookSaveDto bookCreateDto = new BookSaveDto(1, null, 1L, 1L);
        String expectedResult = mapper.writeValueAsString(bookCreateDto);

        ErrorDto errorDto = new ErrorDto(400, "The title should not be empty");

        mvc.perform(post("/api/book")
                        .contentType(APPLICATION_JSON)
                        .content(expectedResult))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(mapper.writeValueAsString(errorDto)));
    }

    @DisplayName("должен при создании книги вернуть ошибку, если заголовок очень длинный")
    @Test
    void shouldReturnErrorLongTitleForCreateBook() throws Exception {
        String longString = ".".repeat(300);
        BookSaveDto bookCreateDto = new BookSaveDto(1, longString, 1L, 1L);
        String expectedResult = mapper.writeValueAsString(bookCreateDto);

        ErrorDto errorDto = new ErrorDto(400, "Title must contain from 1 to 256 characters");

        mvc.perform(post("/api/book")
                        .contentType(APPLICATION_JSON)
                        .content(expectedResult))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(mapper.writeValueAsString(errorDto)));
    }

    @DisplayName("должен обновить книгу")
    @Test
    void shouldUpdateBook() throws Exception {
        BookSaveDto bookUpdateDto = new BookSaveDto(1L, "update_book_title", 1L, 1L);

        BookDto bookDto = new BookDto(1L, "update_book_title",
                new AuthorDto(1L, "Author"), new GenreDto(1L, "Genre"));

        when(bookService.update(bookUpdateDto)).thenReturn(bookDto);

        String expectedResult = mapper.writeValueAsString(bookUpdateDto);

        mvc.perform(put("/api/book")
                        .contentType(APPLICATION_JSON)
                        .content(expectedResult))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookDto)));
    }

    @DisplayName("должен удалить книгу")
    @Test
    void shouldDeleteBookAndRedirectMainPage() throws Exception {
        mvc.perform(delete("/api/book/1"))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).deleteById(1);
    }

    private List<BookDto> createBookList() {
        List<BookDto> bookDtoList = new ArrayList<>();

        for (int i = 1; i < 5; i++) {
            BookDto bookDto = new BookDto();
            bookDto.setId(i);
            bookDto.setTitle("Title_" + i);
            bookDto.setAuthor(new AuthorDto(i, "Author_" + i));
            bookDto.setGenre(new GenreDto(i, "Genre_" + i));
            bookDtoList.add(bookDto);
        }

        return bookDtoList;
    }
}
