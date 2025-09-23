package ru.otus.hw.repositories.relation;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.migrate.AuthorMigrate;

public interface AuthorIdMigrateRepository extends JpaRepository<AuthorMigrate, String> {

    AuthorMigrate findByMongoId(String mongoId);
}
