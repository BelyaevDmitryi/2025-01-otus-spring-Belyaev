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
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookSaveDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BookController {
    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    private final BookMapper bookMapper;

    @GetMapping("/")
    public String mainPage(Model model) {
        List<BookDto> books = bookService.findAll();
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping(value = "/book/{id}")
    public String bookEditPage(@PathVariable("id") long id, Model model) {
        BookDto book = bookService.findById(id);
        List<AuthorDto> authors = authorService.findAll();
        List<GenreDto> genres = genreService.findAll();

        model.addAttribute("book", bookMapper.toSaveDto(book));
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "book_edit";
    }

    @GetMapping(value = "/book")
    public String bookNewPage(Model model) {
        List<AuthorDto> authors = authorService.findAll();
        List<GenreDto> genres = genreService.findAll();

        model.addAttribute("book", new BookSaveDto());
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "book_edit";
    }

    @PostMapping(value = "/book")
    public String saveBook(@Valid @ModelAttribute("book") BookSaveDto book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<AuthorDto> authors = authorService.findAll();
            List<GenreDto> genres = genreService.findAll();
            model.addAttribute("book", book);
            model.addAttribute("authors", authors);
            model.addAttribute("genres", genres);
            return "book_edit";
        }

        if (book.getId() == 0) {
            bookService.create(book);
        } else {
            bookService.update(book);
        }

        return "redirect:/";
    }

    @PostMapping(value = "/book/{id}/delete")
    public String deleteBook(@PathVariable("id") long id) {
        bookService.deleteById(id);
        return "redirect:/";
    }

}
