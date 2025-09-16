package ru.otus.hw.repositories.relation;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.migrate.BookMigrate;

public interface BookIdMigrateRepository extends JpaRepository<BookMigrate, String> {
    BookMigrate findByMongoId(String mongoId);
}
