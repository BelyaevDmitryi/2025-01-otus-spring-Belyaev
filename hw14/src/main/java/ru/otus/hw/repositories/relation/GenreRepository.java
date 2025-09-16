package ru.otus.hw.repositories.relation;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.relation.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {

}
