package ru.otus.hw.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentSaveDto;
import ru.otus.hw.services.CommentService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CommentControllerRest {
    private final CommentService commentService;

    @GetMapping(value = "/api/books/{id}/comments")
    public ResponseEntity<List<CommentDto>> commentByBookId(@PathVariable("id") long id) {
        List<CommentDto> comments = commentService.findByBookId(id);
        return ResponseEntity.ok(comments);
    }

    @GetMapping(value = "/api/comments/{id}")
    public ResponseEntity<CommentDto> commentById(@PathVariable("id") long id) {
        return ResponseEntity.ok(commentService.findById(id));
    }

    @PostMapping("/api/comment")
    public ResponseEntity<CommentDto> createComment(@Valid @RequestBody CommentSaveDto commentSaveDto) {
        var commentDto = commentService.create(commentSaveDto);
        return ResponseEntity.ok(commentDto);
    }

    @PutMapping("/api/comment")
    public ResponseEntity<CommentDto> updateComment(@Valid @RequestBody CommentSaveDto commentSaveDto) {
        var commentDto = commentService.update(commentSaveDto);
        return ResponseEntity.ok(commentDto);
    }

    @DeleteMapping("/api/comment/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable("id") long id) {
        commentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
