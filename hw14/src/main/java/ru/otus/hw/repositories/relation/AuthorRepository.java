package ru.otus.hw.repositories.relation;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.relation.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Author findById(long id);
}
