package ru.otus.hw.services;

import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookSaveDto;

import java.util.List;

public interface BookService {
    BookDto findById(long id);

    List<BookDto> findAll();

    BookDto create(BookSaveDto bookSaveDto);

    BookDto update(BookSaveDto bookSaveDto);

    void deleteById(long id);

    public boolean existsById(Long id);
}
