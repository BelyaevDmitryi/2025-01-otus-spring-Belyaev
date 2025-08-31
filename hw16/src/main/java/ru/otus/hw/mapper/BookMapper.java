package ru.otus.hw.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookSaveDto;
import ru.otus.hw.models.Book;

@RequiredArgsConstructor
@Component
public class BookMapper {

    private final AuthorMapper authorMapper;

    private final GenreMapper genreMapper;

    public BookDto fromModel(Book book) {
        return new BookDto(book.getId(), book.getTitle(),
                authorMapper.fromModel(book.getAuthor()),
                genreMapper.fromModel(book.getGenre()));
    }

    public Book toModel(BookDto bookDto) {
        return new Book(bookDto.getId(), bookDto.getTitle(),
                authorMapper.toModel(bookDto.getAuthor()),
                genreMapper.toModel(bookDto.getGenre()));
    }

    public BookSaveDto toSaveDto(BookDto bookDto) {
        return new BookSaveDto(bookDto.getId(), bookDto.getTitle(),
                bookDto.getAuthor().getId(),
                bookDto.getGenre().getId());
    }
}
