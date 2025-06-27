package mate.academy.bookshop.book;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;
import mate.academy.bookshop.dto.book.BookDto;
import mate.academy.bookshop.dto.book.CreateBookRequestDto;
import mate.academy.bookshop.model.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext context) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Get all books with pagination should succeed")
    @Sql(scripts = {
            "classpath:database/books/insert-three-books.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/delete-from-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAllBooks_IgnoringCategories_ShouldReturnPaginationBook() throws Exception {
        MvcResult result = mockMvc.perform(get("/books")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        Assertions.assertTrue(response.contains("content"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Get book by id should return correct book")
    @Sql(scripts = {
            "classpath:database/books/insert-single-book.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/delete-from-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getBookById_WithValidId_ShouldReturnBookDto() throws Exception {
        mockMvc.perform(get("/books/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Create a book with valid DTO should succeed")
    @Sql(scripts = "classpath:database/categories/insert-two-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/bookscategories/delete-from-books-categories.sql",
            "classpath:database/books/delete-from-books.sql",
            "classpath:database/categories/delete-from-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createBook_validRequestDto_successful() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("New Book");
        requestDto.setAuthor("Author");
        requestDto.setPrice(java.math.BigDecimal.TEN);
        requestDto.setIsbn("1234567890");
        requestDto.setDescription("Desc");
        requestDto.setCoverImage("");

        Category fiction = new Category();
        fiction.setId(1L);
        fiction.setName("Fiction");
        fiction.setDescription("Fiction books");

        Category science = new Category();
        science.setId(2L);
        science.setName("Science");
        science.setDescription("Science books");

        requestDto.setCategories(Set.of(fiction, science));

        String json = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        Assertions.assertEquals("New Book", actual.getTitle());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Delete existing book should return no content")
    @Sql(scripts = {
            "classpath:database/books/insert-single-book.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/delete-from-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteBook_bookExistsInDb_successful() throws Exception {
        mockMvc.perform(delete("/books/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Update book with valid request should succeed")
    @Sql(scripts = {
            "classpath:database/categories/insert-two-categories.sql",
            "classpath:database/books/insert-single-book.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/bookscategories/delete-from-books-categories.sql",
            "classpath:database/books/delete-from-books.sql",
            "classpath:database/categories/delete-from-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateBook_bookExistInDb_successful() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Updated Book");
        requestDto.setAuthor("Updated Author");
        requestDto.setPrice(java.math.BigDecimal.valueOf(20));
        requestDto.setIsbn("9876543210");
        requestDto.setDescription("Updated desc");
        requestDto.setCoverImage("");

        Category fiction = new Category();
        fiction.setId(1L);
        fiction.setName("Fiction");
        fiction.setDescription("Fiction books");

        Category science = new Category();
        science.setId(2L);
        science.setName("Science");
        science.setDescription("Science books");

        requestDto.setCategories(Set.of(fiction, science));

        String json = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(put("/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);

        Assertions.assertEquals("Updated Book", actual.getTitle());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Search books with parameters should return filtered list")
    @Sql(scripts = {
            "classpath:database/books/insert-three-books.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/delete-from-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void searchBooks_bookExistInDb_successful() throws Exception {
        MvcResult result = mockMvc.perform(get("/books/search")
                        .param("author", "Author 1")
                        .param("title", "Book")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        Assertions.assertTrue(response.contains("Author 1"));
    }
}
