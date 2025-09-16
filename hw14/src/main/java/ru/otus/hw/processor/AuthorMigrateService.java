package ru.otus.hw.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.migrate.AuthorMigrate;
import ru.otus.hw.models.mongo.MongoAuthor;
import ru.otus.hw.models.relation.Author;
import ru.otus.hw.repositories.relation.AuthorIdMigrateRepository;
import ru.otus.hw.repositories.relation.AuthorRepository;

@RequiredArgsConstructor
@Service
public class AuthorMigrateService {

    private final AuthorRepository authorRepository;

    private final AuthorIdMigrateRepository authorIdMigrateRepository;

    public Author save(MongoAuthor mongoAuthor) {
        Author author = new Author();
        author.setFullName(mongoAuthor.getFullName());

        Author savedAuthor = authorRepository.save(author);

        AuthorMigrate authorMigrate = new AuthorMigrate(savedAuthor.getId(), mongoAuthor.getId());
        authorIdMigrateRepository.save(authorMigrate);
        return savedAuthor;
    }

    public Author getAuthorByMongoId(String mongoAuthorId) {
        AuthorMigrate authorMigrate = authorIdMigrateRepository.findByMongoId(mongoAuthorId);
        return authorRepository.findById(authorMigrate.getId());
    }
}
