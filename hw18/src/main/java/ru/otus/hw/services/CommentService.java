package ru.otus.hw.services;

import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentSaveDto;

import java.util.List;

public interface CommentService {

    CommentDto findById(long id);

    List<CommentDto> findByBookId(long bookId);

    CommentDto create(CommentSaveDto commentSaveDto);

    CommentDto update(CommentSaveDto commentSaveDto);

    void deleteById(long id);
}
