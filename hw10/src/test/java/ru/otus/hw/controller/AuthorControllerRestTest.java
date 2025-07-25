package ru.otus.hw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("REST Контроллер для авторов")
@WebMvcTest(AuthorControllerRest.class)
class AuthorControllerRestTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private AuthorService authorService;

    @DisplayName("должен отдать список авторов")
    @Test
    void shouldReturnAuthorList() throws Exception {
        List<AuthorDto> authorDtoList = createAuthorList();

        when(authorService.findAll()).thenReturn(authorDtoList);

        mvc.perform(get("/api/author"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(authorDtoList)));
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
}
