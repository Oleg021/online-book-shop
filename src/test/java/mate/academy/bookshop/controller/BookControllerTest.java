package mate.academy.bookshop.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import mate.academy.bookshop.dto.book.BookDto;
import mate.academy.bookshop.dto.book.CreateBookRequestDto;
import mate.academy.bookshop.util.BookUtil;
import org.apache.commons.lang3.builder.EqualsBuilder;
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

        Map<String, Object> pageResponse = objectMapper.readValue(
                response, new TypeReference<>() {});
        List<Map<String, Object>> content = (List<Map<String, Object>>) pageResponse.get("content");

        assertEquals(3, content.size());
        assertEquals("Book One", content.get(0).get("title"));
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
        MvcResult result = mockMvc.perform(get("/books/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        BookDto actual = objectMapper.readValue(response, BookDto.class);

        assertEquals(1L, actual.getId());
        assertEquals("Test Book", actual.getTitle());
        assertEquals("Author 1", actual.getAuthor());
        assertEquals("1111111111", actual.getIsbn());
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
        CreateBookRequestDto requestDto = BookUtil.buildCreateBookRequestDto();
        BookDto expected = BookUtil.createBookDto();

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        post("/books")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class
        );

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
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
        CreateBookRequestDto requestDto = BookUtil.buildUpdateBookRequestDto();

        String json = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(put("/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);

        BookDto expected = BookUtil.buildExpectedBookDto(requestDto, actual);

        assertEquals(expected, actual);
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
        assertTrue(response.contains("Author 1"));
    }
}
