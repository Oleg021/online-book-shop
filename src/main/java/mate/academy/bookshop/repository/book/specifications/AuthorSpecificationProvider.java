package mate.academy.bookshop.repository.book.specifications;

import static mate.academy.bookshop.repository.book.BookSpecificationBuilder.AUTHOR;

import mate.academy.bookshop.model.Book;
import mate.academy.bookshop.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {

    @Override
    public String getKey() {
        return AUTHOR;
    }

    @Override
    public Specification<Book> getSpecification(String param) {
        return (root, query, criteriaBuilder) -> root.get(AUTHOR).in(param);
    }
}
