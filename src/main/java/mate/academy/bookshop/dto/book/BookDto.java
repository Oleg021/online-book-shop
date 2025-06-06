package mate.academy.bookshop.dto.book;

import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;
import mate.academy.bookshop.model.Category;

@Data
public class BookDto {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private String description;
    private String coverImage;
    private Set<Category> categories;
}
