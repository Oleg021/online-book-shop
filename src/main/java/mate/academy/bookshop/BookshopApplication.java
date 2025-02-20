package mate.academy.bookshop;

import java.math.BigDecimal;
import mate.academy.bookshop.model.Book;
import mate.academy.bookshop.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EntityScan(basePackages = "mate.academy.bookshop.model")
public class BookshopApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(BookshopApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book = new Book();
            book.setPrice(BigDecimal.valueOf(500));
            book.setTitle("Atlas Shrugged");
            book.setAuthor("Ayn Rand");
            book.setDescription("Very nice book !!!");
            book.setCoverImage("https://static.yakaboo.ua/media/catalog/product/4/9/497069_69650593.jpg");
            book.setIsbn("9780525934189");
            bookService.save(book);
        };
    }
}
