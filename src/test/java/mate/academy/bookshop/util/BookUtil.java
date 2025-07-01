package mate.academy.bookshop.util;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import mate.academy.bookshop.dto.book.BookDto;
import mate.academy.bookshop.dto.book.CreateBookRequestDto;
import mate.academy.bookshop.model.Book;
import mate.academy.bookshop.model.Category;

public class BookUtil {
    public static CreateBookRequestDto buildCreateBookRequestDto() {
        CreateBookRequestDto dto = new CreateBookRequestDto();
        dto.setTitle("New Book");
        dto.setAuthor("Author");
        dto.setPrice(BigDecimal.TEN);
        dto.setIsbn("1234567890");
        dto.setDescription("Desc");
        dto.setCoverImage("");

        Category fiction = new Category();
        fiction.setId(1L);
        fiction.setName("Fiction");
        fiction.setDescription("Fiction books");

        Category science = new Category();
        science.setId(2L);
        science.setName("Science");
        science.setDescription("Science books");

        dto.setCategories(Set.of(fiction, science));
        return dto;
    }

    public static BookDto createBookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("New Book");
        bookDto.setAuthor("Author");
        bookDto.setIsbn("1234567890");
        bookDto.setPrice(BigDecimal.TEN);
        return bookDto;
    }

    public static BookDto createExpectedBookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Test Book");
        bookDto.setAuthor("Author 1");
        bookDto.setIsbn("1111111111");
        bookDto.setPrice(BigDecimal.valueOf(10.0).setScale(2));
        bookDto.setDescription("Test description");
        bookDto.setCoverImage("");
        bookDto.setCategories(new HashSet<>());
        return bookDto;
    }

    public static CreateBookRequestDto buildUpdateBookRequestDto() {
        CreateBookRequestDto dto = new CreateBookRequestDto();
        dto.setTitle("Updated Book");
        dto.setAuthor("Updated Author");
        dto.setPrice(BigDecimal.valueOf(20));
        dto.setIsbn("9876543210");
        dto.setDescription("Updated desc");
        dto.setCoverImage("");

        Category fiction = new Category();
        fiction.setId(1L);
        fiction.setName("Fiction");
        fiction.setDescription("Fiction books");

        Category science = new Category();
        science.setId(2L);
        science.setName("Science");
        science.setDescription("Science books");

        dto.setCategories(Set.of(fiction, science));
        return dto;
    }

    public static BookDto buildExpectedBookDto(CreateBookRequestDto request, BookDto actual) {
        BookDto dto = new BookDto();
        dto.setId(actual.getId());
        dto.setTitle(request.getTitle());
        dto.setAuthor(request.getAuthor());
        dto.setIsbn(request.getIsbn());
        dto.setDescription(request.getDescription());
        dto.setCoverImage(request.getCoverImage());
        dto.setPrice(request.getPrice());
        dto.setCategories(actual.getCategories());
        return dto;
    }

    public static Book buildBookFromRequest(CreateBookRequestDto dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setPrice(dto.getPrice());
        book.setIsbn(dto.getIsbn());
        book.setDescription(dto.getDescription());
        book.setCoverImage(dto.getCoverImage());
        return book;
    }

    public static Book buildSavedBook(Book book) {
        Book savedBook = new Book();
        savedBook.setId(1L);
        savedBook.setTitle(book.getTitle());
        savedBook.setAuthor(book.getAuthor());
        savedBook.setPrice(book.getPrice());
        savedBook.setIsbn(book.getIsbn());
        savedBook.setDescription(book.getDescription());
        savedBook.setCoverImage(book.getCoverImage());
        return savedBook;
    }

    public static BookDto buildBookDto(Book savedBook) {
        return new BookDto()
                .setId(savedBook.getId())
                .setTitle(savedBook.getTitle())
                .setAuthor(savedBook.getAuthor())
                .setPrice(savedBook.getPrice())
                .setIsbn(savedBook.getIsbn())
                .setDescription(savedBook.getDescription())
                .setCoverImage(savedBook.getCoverImage());
    }
}
