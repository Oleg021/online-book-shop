package mate.academy.bookshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import mate.academy.bookshop.dto.book.BookDto;
import mate.academy.bookshop.dto.book.BookSearchParameters;
import mate.academy.bookshop.dto.book.CreateBookRequestDto;
import mate.academy.bookshop.exceptions.EntityNotFoundException;
import mate.academy.bookshop.mapper.BookMapper;
import mate.academy.bookshop.model.Book;
import mate.academy.bookshop.repository.book.BookRepository;
import mate.academy.bookshop.repository.book.BookSpecificationBuilder;
import mate.academy.bookshop.service.impl.BookServiceImpl;
import mate.academy.bookshop.util.BookUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    public void save_WithValidRequest_ShouldReturnSaveBook() {
        CreateBookRequestDto requestDto = BookUtil.buildCreateBookRequestDto();
        Book book = BookUtil.buildBookFromRequest(requestDto);
        Book savedBook = BookUtil.buildSavedBook(book);
        BookDto savedBookDto = BookUtil.buildBookDto(savedBook);

        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(bookMapper.toDto(savedBook)).thenReturn(savedBookDto);

        BookDto actual = bookService.save(requestDto);

        assertNotNull(actual);
        assertEquals(savedBookDto, actual);

        verify(bookMapper).toModel(requestDto);
        verify(bookRepository).save(book);
        verify(bookMapper).toDto(savedBook);
    }

    @Test
    public void findAllBooks_WithPageable_ShouldReturnAllBooks() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> books = List.of(new Book(), new Book());
        Page<Book> bookPage = new PageImpl<>(books);
        when(bookRepository.findAll(pageable)).thenReturn(bookPage);

        List<BookDto> bookDtos = List.of(new BookDto(), new BookDto());
        when(bookMapper.toDto(Mockito.any(Book.class)))
                .thenAnswer(invocation -> {
                    Book book = invocation.getArgument(0);
                    int index = books.indexOf(book);
                    return bookDtos.get(index);
                });

        Page<BookDto> result = bookService.findAll(pageable);

        verify(bookRepository).findAll(pageable);
        verify(bookMapper, Mockito.times(books.size())).toDto(Mockito.any(Book.class));

        assertEquals(bookDtos, result.getContent());
    }

    @Test
    public void findBookById_WithValidId_ShouldReturnValidBook() {
        Long bookId = 1L;

        CreateBookRequestDto requestDto = BookUtil.buildCreateBookRequestDto();
        Book book = BookUtil.buildBookFromRequest(requestDto);
        book.setId(bookId);
        book.setCategories(Set.of());
        book.setDeleted(false);

        BookDto expectedDto = BookUtil.buildBookDto(book);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expectedDto);

        BookDto actual = bookService.findById(bookId);

        assertNotNull(actual);
        assertEquals(expectedDto, actual);

        verify(bookRepository).findById(bookId);
        verify(bookMapper).toDto(book);
    }

    @Test
    public void findBookById_WithInvalidId_ShouldReturnException() {
        Long bookId = 10L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> bookService.findById(bookId)
        );
        String expected = "Could not find book with id: " + bookId;
        String actual = exception.getMessage();
        assertEquals(expected, actual);
    }

    @Test
    public void deleteById_WithValidId_ShouldDeleteBook() {
        Long bookId = 1L;
        bookService.deleteById(bookId);
        verify(bookRepository).deleteById(bookId);
    }

    @Test
    public void update_WithValidIdAndRequest_ShouldUpdateAndReturnBook() {
        Long bookId = 1L;
        CreateBookRequestDto requestDto = BookUtil.buildUpdateBookRequestDto();

        Book existingBook = BookUtil.buildBookFromRequest(BookUtil.buildCreateBookRequestDto());
        existingBook.setId(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));

        bookService.update(bookId, requestDto);

        verify(bookMapper).updateBookFromDto(requestDto, existingBook);
        verify(bookRepository).save(existingBook);
        verify(bookMapper).toDto(existingBook);
    }

    @Test
    public void searchBookByParam_WithValidParamsAndPageable_ShouldReturnValidBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Harry Potter");
        book.setAuthor("J.K. Rowling");
        book.setIsbn("123456789");
        book.setPrice(BigDecimal.valueOf(19.99));
        book.setDescription("A fantasy book");
        book.setCoverImage("cover.jpg");
        book.setCategories(Set.of());
        book.setDeleted(false);

        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setPrice(book.getPrice());
        bookDto.setDescription(book.getDescription());
        bookDto.setCoverImage(book.getCoverImage());
        bookDto.setCategories(book.getCategories());

        BookSearchParameters params = new BookSearchParameters("J.K. Rowling", "Harry");
        Specification<Book> specification = Specification.where(null);
        Pageable pageable = PageRequest.of(0, 10);

        when(bookSpecificationBuilder.build(params)).thenReturn(specification);

        Page<Book> bookPage = new PageImpl<>(List.of(book), pageable, 1);
        when(bookRepository.findAll(specification, pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        List<BookDto> result = bookService.search(params, pageable);

        List<BookDto> expected = List.of(bookDto);
        assertEquals(expected, result);
        verify(bookSpecificationBuilder).build(params);
        verify(bookRepository).findAll(specification, pageable);
        verify(bookMapper,
                Mockito.times(bookPage.getContent().size()))
                .toDto(Mockito.any(Book.class));
    }
}
