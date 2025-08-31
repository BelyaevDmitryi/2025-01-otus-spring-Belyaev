package ru.otus.hw.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentSaveDto;
import ru.otus.hw.mapper.CommentMapper;
import ru.otus.hw.services.CommentService;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class CommentController {
    private final CommentService commentService;

    private final CommentMapper commentMapper;

    @GetMapping(value = "/comment/book/{id}")
    public String commentForBookPage(@PathVariable("id") long id, Model model) {
        List<CommentDto> comments = commentService.findByBookId(id);
        model.addAttribute("comments", comments);
        model.addAttribute("bookId", id);
        return "comments";
    }

    @GetMapping(value = "/comment/{id}")
    public String commentEditPage(@PathVariable("id") long id, Model model) {
        CommentDto commentDto = commentService.findById(id);

        model.addAttribute("comment", commentMapper.toSaveDto(commentDto));
        return "comment_edit";
    }

    @PostMapping(value = "/comment")
    public String saveComment(@Valid @ModelAttribute("comment") CommentSaveDto commentSaveDto,
                              BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("comment", commentSaveDto);
            return "comment_edit";
        }

        CommentDto savedCommentDto = null;

        if (commentSaveDto.getId() == 0) {
            savedCommentDto = commentService.create(commentSaveDto);
        } else {
            CommentDto commentDto = commentMapper.fromSaveDtoToDto(commentSaveDto);
            savedCommentDto = commentService.update(commentDto);
        }

        return "redirect:/comment/book/" + savedCommentDto.getBook().getId();
    }

    @PostMapping(value = "/comment/{id}/delete")
    public String deleteComment(@PathVariable("id") long id, Model model) {
        CommentDto commentDto = commentService.findById(id);
        commentService.delete(commentDto);
        return "redirect:/comment/book/" + commentDto.getBook().getId();
    }

    @GetMapping(value = "/comment/book/{id}/create")
    public String commentNewPage(@PathVariable("id") long id, Model model) {
        CommentSaveDto commentSaveDto = new CommentSaveDto();
        commentSaveDto.setBookId(id);

        model.addAttribute("comment", commentSaveDto);
        return "comment_edit";
    }

}
