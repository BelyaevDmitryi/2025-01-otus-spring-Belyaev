package ru.otus.hw.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.otus.hw.controller.BookController;
import ru.otus.hw.dto.BookSaveDto;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.models.Authority;
import ru.otus.hw.models.User;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Безопасность. Контроллер для книг")
@WebMvcTest(BookController.class)
@Import({SecurityConfiguration.class})
public class BookControllerSecurityTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private AuthorService authorService;

    @MockitoBean
    private GenreService genreService;

    @MockitoBean
    private BookMapper bookMapper;


    @DisplayName("должен проверить для главной страницы корректность возвращаемого статуса")
    @ParameterizedTest(name = "{2} для пользователя {0}")
    @MethodSource("createTestDataForMainPage")
    void shouldVerifyCorrectReturnedStatusForMainPage(String user, List<GrantedAuthority> authorityList,
                                                      int status) throws Exception {

        var request = MockMvcRequestBuilders.get("/");

        checkStatusAndRedirect(user, authorityList, status, request);
    }

    @DisplayName("должен проверить для страницы создания корректность возвращаемого статуса")
    @ParameterizedTest(name = "{2} для пользователя {0}")
    @MethodSource("createTestDataForEditCreateSaveBook")
    void shouldVerifyCorrectReturnedStatusForCreateBookPage(String user, List<GrantedAuthority> authorityList,
                                                            int status) throws Exception {

        var request = MockMvcRequestBuilders.get("/book");

        checkStatusAndRedirect(user, authorityList, status, request);
    }

    @DisplayName("должен проверить для страницы редактирования корректность возвращаемого статуса")
    @ParameterizedTest(name = "{2} для пользователя {0}")
    @MethodSource("createTestDataForEditCreateSaveBook")
    void shouldVerifyCorrectReturnedStatusForEditBookPage(String user, List<GrantedAuthority> authorityList,
                                                          int status) throws Exception {

        when(bookMapper.toSaveDto(any())).thenReturn(new BookSaveDto(1, "title", 1, 1));

        var request = MockMvcRequestBuilders.get("/book/1");

        checkStatusAndRedirect(user, authorityList, status, request);
    }

    @DisplayName("должен проверить при сохранении корректность возвращаемого статуса")
    @ParameterizedTest(name = "{2} для пользователя {0}")
    @MethodSource("createTestDataForEditCreateSaveBook")
    void shouldVerifyCorrectReturnedStatusForSaveBook(String user, List<GrantedAuthority> authorityList,
                                                      int status) throws Exception {
        var request = MockMvcRequestBuilders.post("/book");

        checkStatusAndRedirect(user, authorityList, status, request);
    }

    @DisplayName("должен проверить при удалении корректность возвращаемого статуса")
    @ParameterizedTest(name = "{2} для пользователя {0}")
    @MethodSource("createTestDataForDeleteBook")
    void shouldVerifyCorrectReturnedStatusForDeleteBook(String user, List<GrantedAuthority> authorityList,
                                                        int status) throws Exception {
        var request = MockMvcRequestBuilders.post("/book/1/delete");

        checkStatusAndRedirect(user, authorityList, status, request);
    }

    private void checkStatusAndRedirect(String user, List<GrantedAuthority> authorityList, int status,
                                        MockHttpServletRequestBuilder request) throws Exception {
        if (user != null) {
            request = request.with(user(user).authorities(authorityList));
        }

        ResultActions resultActions = mvc.perform(request).andExpect(status().is(status));

        if (Objects.isNull(user)) {
            resultActions.andExpect(redirectedUrlPattern("**/login"));
        }
    }

    public static Stream<Arguments> createTestDataForMainPage() {
        var libUser = new User("lib", List.of(new Authority("BOOK_EDITOR")));
        var userUser = new User("user", List.of(new Authority("COMMENT_EDITOR")));

        return Stream.of(
                Arguments.of(null, null, 302),
                Arguments.of(libUser.getUsername(), libUser.getAuthorityList(), 200),
                Arguments.of(userUser.getUsername(), userUser.getAuthorityList(), 200)
        );
    }

    public static Stream<Arguments> createTestDataForEditCreateSaveBook() {
        var libUser = new User("lib", List.of(new Authority("BOOK_EDITOR")));
        var userUser = new User("user", List.of(new Authority("COMMENT_EDITOR")));

        return Stream.of(
                Arguments.of(null, null, 302),
                Arguments.of(libUser.getUsername(), libUser.getAuthorityList(), 200),
                Arguments.of(userUser.getUsername(), userUser.getAuthorityList(), 403)
        );
    }


    public static Stream<Arguments> createTestDataForDeleteBook() {
        var libUser = new User("lib", List.of(new Authority("BOOK_EDITOR")));
        var userUser = new User("user", List.of(new Authority("COMMENT_EDITOR")));

        return Stream.of(
                Arguments.of(null, null, 302),
                Arguments.of(libUser.getUsername(), libUser.getAuthorityList(), 302),
                Arguments.of(userUser.getUsername(), userUser.getAuthorityList(), 403)
        );
    }
}
