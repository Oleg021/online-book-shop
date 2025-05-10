package mate.academy.bookshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookshop.dto.book.BookDto;
import mate.academy.bookshop.dto.book.BookSearchParameters;
import mate.academy.bookshop.dto.book.CreateBookRequestDto;
import mate.academy.bookshop.service.BookService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book management", description = "Endpoints for managing books")
@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @Operation(summary = "Get all books",
            description = "Get list of all books")
    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<BookDto> getAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @Operation(summary = "Get book",
            description = "Get specific book by id")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public BookDto getById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @Operation(summary = "Create a new book",
            description = "Create a new book")
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto bookDto) {
        return bookService.save(bookDto);
    }

    @Operation(summary = "Delete book",
            description = "Delete specific book by id")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update book",
            description = "Update specific book by id")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public BookDto update(@PathVariable Long id, @RequestBody @Valid CreateBookRequestDto bookDto) {
        return bookService.update(id, bookDto);
    }

    @Operation(summary = "Search books",
            description = "Search specific books using parameters")
    @GetMapping("/search")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<BookDto> search(BookSearchParameters searchParameters, Pageable pageable) {
        return bookService.search(searchParameters, pageable);
    }
}
