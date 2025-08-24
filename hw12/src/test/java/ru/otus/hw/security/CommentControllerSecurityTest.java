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
import ru.otus.hw.controller.CommentController;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentSaveDto;
import ru.otus.hw.mapper.CommentMapper;
import ru.otus.hw.models.Authority;
import ru.otus.hw.models.User;
import ru.otus.hw.services.CommentService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Безопасность. Контроллер для комментариев")
@WebMvcTest(CommentController.class)
@Import({SecurityConfiguration.class})
public class CommentControllerSecurityTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CommentService commentService;

    @MockitoBean
    private CommentMapper commentMapper;


    @DisplayName("должен проверить для страницы комментариев для книги корректность возвращаемого статуса")
    @ParameterizedTest(name = "{2} для пользователя {0}")
    @MethodSource("createTestDataForMainPage")
    void shouldVerifyCorrectReturnedStatusForMainPage(String user, List<GrantedAuthority> authorityList,
                                                      int status) throws Exception {

        var request = MockMvcRequestBuilders.get("/comment/book/1");

        checkStatusAndRedirect(user, authorityList, status, request);
    }

    @DisplayName("должен проверить для страницы редактирования корректность возвращаемого статуса")
    @ParameterizedTest(name = "{2} для пользователя {0}")
    @MethodSource("createTestDataForEditCreateSave")
    void shouldVerifyCorrectReturnedStatusForEditPage(String user, List<GrantedAuthority> authorityList,
                                                      int status) throws Exception {

        when(commentMapper.toSaveDto(any())).thenReturn(new CommentSaveDto(1, "comment", 1));

        var request = MockMvcRequestBuilders.get("/comment/1");

        checkStatusAndRedirect(user, authorityList, status, request);
    }

    @DisplayName("должен проверить для страницы создания корректность возвращаемого статуса")
    @ParameterizedTest(name = "{2} для пользователя {0}")
    @MethodSource("createTestDataForEditCreateSave")
    void shouldVerifyCorrectReturnedStatusForCreatePage(String user, List<GrantedAuthority> authorityList,
                                                        int status) throws Exception {

        var request = MockMvcRequestBuilders.get("/comment/book/1/create");

        checkStatusAndRedirect(user, authorityList, status, request);
    }

    @DisplayName("должен проверить для сохранения корректность возвращаемого статуса")
    @ParameterizedTest(name = "{2} для пользователя {0}")
    @MethodSource("createTestDataForEditCreateSave")
    void shouldVerifyCorrectReturnedStatusForSavePage(String user, List<GrantedAuthority> authorityList,
                                                      int status) throws Exception {

        var request = MockMvcRequestBuilders.post("/comment").with(csrf());

        checkStatusAndRedirect(user, authorityList, status, request);
    }

    @DisplayName("должен проверить для удаления корректность возвращаемого статуса")
    @ParameterizedTest(name = "{2} для пользователя {0}")
    @MethodSource("createTestDataForDelete")
    void shouldVerifyCorrectReturnedStatusForDeletePage(String user, List<GrantedAuthority> authorityList,
                                                        int status) throws Exception {

        when(commentService.findById(1)).thenReturn(new CommentDto(1, "comment", new BookDto()));

        var request = MockMvcRequestBuilders.post("/comment/1/delete").with(csrf());

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
        var libUser = new User("lib", List.of(new Authority("ROLE_BOOK_EDITOR")));
        var userUser = new User("user", List.of(new Authority("ROLE_COMMENT_EDITOR")));

        return Stream.of(
                Arguments.of(null, null, 302),
                Arguments.of(libUser.getUsername(), libUser.getAuthorityList(), 200),
                Arguments.of(userUser.getUsername(), userUser.getAuthorityList(), 200)
        );
    }

    public static Stream<Arguments> createTestDataForEditCreateSave() {
        var libUser = new User("lib", List.of(new Authority("ROLE_BOOK_EDITOR")));
        var userUser = new User("user", List.of(new Authority("ROLE_COMMENT_EDITOR")));

        return Stream.of(
                Arguments.of(null, null, 302),
                Arguments.of(libUser.getUsername(), libUser.getAuthorityList(), 403),
                Arguments.of(userUser.getUsername(), userUser.getAuthorityList(), 200)
        );
    }

    public static Stream<Arguments> createTestDataForDelete() {
        var libUser = new User("lib", List.of(new Authority("ROLE_BOOK_EDITOR")));
        var userUser = new User("user", List.of(new Authority("ROLE_COMMENT_EDITOR")));

        return Stream.of(
                Arguments.of(null, null, 302),
                Arguments.of(libUser.getUsername(), libUser.getAuthorityList(), 403),
                Arguments.of(userUser.getUsername(), userUser.getAuthorityList(), 302)
        );
    }
}
