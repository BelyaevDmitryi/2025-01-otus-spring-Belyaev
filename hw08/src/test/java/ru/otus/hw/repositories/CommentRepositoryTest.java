package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с комментариями для книги")
@DataMongoTest
class CommentRepositoryTest {

    private final String BOOK_NO_EXIST_ID = "book_no_exist_id";

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookRepository bookRepository;

    @DisplayName(" загружает комментарии по id книги ")
    @Test
    void loadComment() {
        List<Comment> emptyListComment = commentRepository.findByBook(BOOK_NO_EXIST_ID);
        assertThat(emptyListComment).isEmpty();

        List<Book> listBook = bookRepository.findAll();
        List<Comment> actualComments = commentRepository.findByBook(listBook.get(0).getId());

        assertThat(actualComments).isNotNull().isNotEmpty()
                .allMatch(comment -> comment.getText() != null
                        && !comment.getText().isEmpty());
    }

}
