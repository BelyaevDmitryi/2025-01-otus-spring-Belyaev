package ru.otus.hw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("REST Контроллер для жанров")
@WebMvcTest(GenreControllerRest.class)
class GenreControllerRestTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private GenreService genreService;

    @DisplayName("должен отдать список жанров")
    @Test
    void shouldReturnGenreList() throws Exception {
        List<GenreDto> genreDtoList = createGenreList();

        when(genreService.findAll()).thenReturn(genreDtoList);

        mvc.perform(get("/api/genre"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(genreDtoList)));
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
