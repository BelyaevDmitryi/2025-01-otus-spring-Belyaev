package ru.otus.hw.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookSaveDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.mapper.AuthorMapper;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.mapper.GenreMapper;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("BookControllerTest")
@WebMvcTest(BookController.class)
@Import({BookMapper.class, AuthorMapper.class, GenreMapper.class})
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private AuthorService authorService;

    @MockitoBean
    private GenreService genreService;

    @Autowired
    private BookMapper bookMapper;


    @DisplayName("должен отдать главную страницу с моделью")
    @Test
    void shouldRenderMainPageWithModel() throws Exception {
        List<BookDto> bookDtoList = createBookList();

        when(bookService.findAll()).thenReturn(bookDtoList);

        mvc.perform(get("/").with(user("user").authorities(new SimpleGrantedAuthority("ROLE_BOOK_EDITOR"))))
                .andExpect(view().name("books"))
                .andExpect(model().attribute("books", bookDtoList));
//        mvc.perform(get("/authenticated")
//                        .with(user("user").authorities(new SimpleGrantedAuthority("BOOK_EDITOR"))))
//                .andExpect(status().isOk());
    }

    @DisplayName("должен отдать страницу для редактирования книги с моделью")
    @Test
    void shouldRenderBookEditPageWithModel() throws Exception {
        List<BookDto> bookDtoList = createBookList();
        BookDto bookDto = bookDtoList.get(0);

        List<AuthorDto> authorDtoList = createAuthorList();
        List<GenreDto> genreDtoList = createGenreList();

        when(bookService.findById(1)).thenReturn(bookDto);
        when(authorService.findAll()).thenReturn(authorDtoList);
        when(genreService.findAll()).thenReturn(genreDtoList);

        mvc.perform(get("/book/1")
                        .with(user("user").authorities(new SimpleGrantedAuthority("ROLE_BOOK_EDITOR")))
                )
                .andExpect(view().name("book_edit"))
                .andExpect(model().attribute("book", bookMapper.toSaveDto(bookDto)))
                .andExpect(model().attribute("authors", authorDtoList))
                .andExpect(model().attribute("genres", genreDtoList));
    }

    @DisplayName("Удалить книгу и сделать redirect на главную страницу")
    @Test
    void shouldDeleteBookAndRedirectMainPage() throws Exception {
        mvc.perform(post("/book/1/delete")
                        .with(user("lib").authorities(new SimpleGrantedAuthority("ROLE_COMMENT_EDITOR")))
                        .with(csrf())
                )
                .andExpect(view().name("redirect:/"));

        verify(bookService, times(1)).deleteById(1);
    }

    @DisplayName("отдать страницу для создания новой книги с моделью")
    @Test
    void shouldRenderNewBookPageWithModel() throws Exception {
        List<AuthorDto> authorDtoList = createAuthorList();
        List<GenreDto> genreDtoList = createGenreList();

        when(authorService.findAll()).thenReturn(authorDtoList);
        when(genreService.findAll()).thenReturn(genreDtoList);

        mvc.perform(get("/book").with(user("user").authorities(new SimpleGrantedAuthority("ROLE_BOOK_EDITOR"))))
                .andExpect(view().name("book_edit"))
                .andExpect(model().attribute("book", new BookSaveDto()))
                .andExpect(model().attribute("authors", authorDtoList))
                .andExpect(model().attribute("genres", genreDtoList));
    }

    @DisplayName("создать книгу и сделать redirect на главную страницу")
    @Test
    void shouldSaveNewBookAndRedirectMainPage() throws Exception {
        BookSaveDto bookSaveDto = new BookSaveDto(0, "new_book_title", 1, 1);

        mvc.perform(post("/book")
                        .with(user("lib").authorities(new SimpleGrantedAuthority("ROLE_COMMENT_EDITOR")))
                        .with(csrf())
                        .param("id", "0")
                        .param("title", "new_book_title")
                        .param("authorId", "1")
                        .param("genreId", "1"))
                .andExpect(view().name("redirect:/"));

        verify(bookService, times(1)).create(bookSaveDto);
    }

    @DisplayName("отдать страницу с ошибкой если будет NotFoundException")
    @Test
    void shouldRenderErrorPage() throws Exception {
        when(bookService.findById(1)).thenThrow(new NotFoundException(""));
        mvc.perform(get("/book/1").with(user("user").authorities(new SimpleGrantedAuthority("ROLE_BOOK_EDITOR"))))
                .andExpect(view().name("error"));
    }

    @DisplayName("сделать redirect на страницу редактирования из-за пустого заголовка книги")
    @Test
    void shouldNotSaveBookWithEmptyTitleAndRedirectEditPage() throws Exception {
        BookSaveDto bookSaveDto = new BookSaveDto(1, "", 1, 1);

        mvc.perform(post("/book")
                        .with(user("user").authorities(new SimpleGrantedAuthority("ROLE_BOOK_EDITOR")))
                        .param("id", "1")
                        .param("title", "")
                        .param("authorId", "1")
                        .param("genreId", "1")
                        .with(csrf())
                )
                .andExpect(view().name("book_edit"));

        verify(bookService, times(0)).update(bookSaveDto);
    }

    @DisplayName("сделать redirect на страницу редактирования из-за длинного заголовка книги")
    @Test
    void shouldNotSaveBookWithLongTitleAndRedirectEditPage() throws Exception {
        String longString = ".".repeat(300);

        BookSaveDto bookSaveDto = new BookSaveDto(1, longString, 1, 1);

        mvc.perform(post("/book")
                        .with(user("lib").authorities(new SimpleGrantedAuthority("ROLE_COMMENT_EDITOR")))
                        .with(csrf())
                        .param("title", longString))
                .andExpect(model().attributeHasFieldErrorCode("book", "title", "Size"))
                .andExpect(view().name("book_edit"));

        verify(bookService, times(0)).update(bookSaveDto);
    }

    private List<BookDto> createBookList() {
        List<BookDto> bookDtoList = new ArrayList<>();

        for (int i = 1; i < 5; i++) {
            BookDto bookDto = new BookDto();
            bookDto.setId(i);
            bookDto.setTitle("Title_" + i);
            bookDto.setAuthor(new AuthorDto());
            bookDto.getAuthor().setId(i);
            bookDto.getAuthor().setFullName("Author_" + i);
            bookDto.setGenre(new GenreDto());
            bookDto.getGenre().setId(i);
            bookDto.getGenre().setName("Genre_" + i);
            bookDtoList.add(bookDto);
        }

        return bookDtoList;
    }

    private List<AuthorDto> createAuthorList() {
        List<AuthorDto> authorDtoList = new ArrayList<>();

        for (int i = 1; i < 5; i++) {
            AuthorDto authorDto = new AuthorDto();
            authorDto.setId(i);
            authorDto.setFullName("Author_" + i);
            authorDtoList.add(authorDto);
        }

        return authorDtoList;
    }

    private List<GenreDto> createGenreList() {
        List<GenreDto> genreDtoList = new ArrayList<>();

        for (int i = 1; i < 5; i++) {
            GenreDto genreDto = new GenreDto();
            genreDto.setId(i);
            genreDto.setName("Genre_" + i);
            genreDtoList.add(genreDto);
        }

        return genreDtoList;
    }
}

