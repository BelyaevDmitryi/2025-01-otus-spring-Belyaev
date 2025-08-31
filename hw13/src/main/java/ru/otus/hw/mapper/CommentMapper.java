package ru.otus.hw.mapper;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentSaveDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

@Component
public class CommentMapper {

    public CommentDto fromModel(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setText(comment.getText());
        commentDto.setId(comment.getId());
        commentDto.setBook(new BookDto());
        commentDto.getBook().setId(comment.getBook().getId());
        return commentDto;
    }

    public Comment toModel(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setId(commentDto.getId());
        comment.setBook(new Book());
        comment.getBook().setId(commentDto.getBook().getId());
        return comment;
    }

    public CommentSaveDto toSaveDto(CommentDto commentDto) {
        CommentSaveDto commentSaveDto = new CommentSaveDto();
        commentSaveDto.setText(commentDto.getText());
        commentSaveDto.setId(commentDto.getId());
        commentSaveDto.setBookId(commentDto.getBook().getId());
        return commentSaveDto;
    }

    public CommentDto fromSaveDtoToDto(CommentSaveDto commentSaveDto) {
        CommentDto commentDto = new CommentDto();
        commentDto.setText(commentSaveDto.getText());
        commentDto.setId(commentSaveDto.getId());
        commentDto.setBook(new BookDto());
        commentDto.getBook().setId(commentSaveDto.getBookId());
        return commentDto;
    }
}
