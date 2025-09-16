package ru.otus.hw.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.mongo.MongoGenre;
import ru.otus.hw.models.relation.Genre;
import ru.otus.hw.repositories.relation.GenreRepository;

@RequiredArgsConstructor
@Service
public class GenreMigrateService {

    private final GenreRepository genreRepository;

    private final CacheManager cacheManager;

    @Cacheable(cacheNames = "genreCache", key = "#mongoGenre.id")
    public Genre save(MongoGenre mongoGenre) {
        Genre genre = new Genre();
        genre.setName(mongoGenre.getName());
        return genreRepository.save(genre);
    }

    public Genre getGenreByMongoId(String mongoGenreId) {
        return cacheManager.getCache("genreCache").get(mongoGenreId, Genre.class);
    }
}
