package ru.otus.hw.events;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("BookRepository с помощью EventsListener")
@DataMongoTest
@Import(BookCascadeDeleteMongoEventListener.class)
class BookRepositoryWithCascadeDeleteEventsListenerTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookRepository bookRepository;

    @DisplayName("должен удалить все комментарии при удалении книги")
    @Test
    void shouldDeleteCommentForBook() {
        List<Book> bookList = bookRepository.findAll();
        Book deletedBook = bookList.get(0);

        List<Comment> actualComments = commentRepository.findByBook(deletedBook.getId());
        assertThat(actualComments).isNotNull().isNotEmpty();

        bookRepository.deleteById(deletedBook.getId());
        bookList.remove(0);
        assertThat(bookList).isNotEmpty();

        List<Comment> deletedComments = commentRepository.findByBook(deletedBook.getId());
        assertThat(deletedComments).isNotNull().isNotEmpty();
        assertThat(bookRepository.findAll())
                .usingRecursiveComparison()
                .isEqualTo(bookList);
    }
}