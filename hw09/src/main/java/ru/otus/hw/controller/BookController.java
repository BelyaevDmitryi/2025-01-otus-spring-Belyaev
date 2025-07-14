package ru.otus.hw.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
            return handleValidationErrors(book, model);
        }

        try {
            bookService.create(book);
            model.addAttribute("successMessage", "Book create successfully!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error creating book: " + e.getMessage());
            return handleValidationErrors(book, model);
        }

        return "redirect:/";
    }

    @PostMapping(value = "/book/{id}")
    public String updateBookPost(@PathVariable("id") Long id,
                                 @Valid @ModelAttribute("book") BookSaveDto book,
                                 BindingResult bindingResult, Model model,
                                 HttpServletRequest request) {

        String method = request.getParameter("_method");
        if ("PUT".equals(method)) {
            return updateBook(id, book, bindingResult, model);
        }

        return "redirect:/book";
    }

    @PutMapping(value = "/book/{id}")
    public String updateBook(@PathVariable("id") Long id,
                             @Valid @ModelAttribute("book") BookSaveDto book,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return handleValidationErrors(book, model);
        }

        if (!bookService.existsById(id)) {
            model.addAttribute("errorMessage", "Book with ID " + id + " not found!");
            return handleValidationErrors(book, model);
        }

        try {
            book.setId(id);
            bookService.update(book);
            model.addAttribute("successMessage", "Book update successfully!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating book: " + e.getMessage());
            return handleValidationErrors(book, model);
        }

        return "redirect:/";
    }

    private String handleValidationErrors(BookSaveDto book, Model model) {
        List<AuthorDto> authors = authorService.findAll();
        List<GenreDto> genres = genreService.findAll();
        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "book_edit";
    }

    @PostMapping(value = "/book/{id}/delete")
    public String deleteBook(@PathVariable("id") long id) {
        bookService.deleteById(id);
        return "redirect:/";
    }

}
