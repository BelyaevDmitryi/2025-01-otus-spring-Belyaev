package ru.otus.hw.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCheckBookFlux() {
        var client = WebClient.create(String.format("http://localhost:%d", port));

        var expectedSize = 3;

        List<String> result = client
                .get().uri("/api/books")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class)
                .take(expectedSize)
                .timeout(Duration.ofSeconds(3))
                .collectList()
                .block();

        assertThat(result).isNotNull().isNotEmpty().hasSize(expectedSize);
    }

    @Test
    void shouldCheckErrorBookById() {
        var client = WebClient.create(String.format("http://localhost:%d", port));

        var result = client
                .get().uri("/api/book/1")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorComplete()
                .block();
    }

    @Test
    void shouldCheckBookById() throws JsonProcessingException {
        var client = WebClient.create(String.format("http://localhost:%d", port));

        var result = client
                .get().uri("/api/book/book_test")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(3))
                .block();

        BookDto bookDto = new BookDto("book_test", "Title_00",
                new AuthorDto("author_test", "Author_00"),
                new GenreDto("genre_test", "Genre_00"));
        assertThat(result).isEqualTo(objectMapper.writeValueAsString(bookDto));
    }
}
