package ru.otus.hw.repositories.relation;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.relation.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    Book findById(long id);

    @Nonnull
    @EntityGraph(value = "book-entity-graph")
    List<Book> findAll();
}
