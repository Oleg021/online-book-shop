package mate.academy.bookshop.repository.book.specifications;

import static mate.academy.bookshop.repository.book.BookSpecificationBuilder.TITLE;

import mate.academy.bookshop.model.Book;
import mate.academy.bookshop.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {

    @Override
    public String getKey() {
        return TITLE;
    }

    @Override
    public Specification<Book> getSpecification(String param) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.like(root.get(TITLE), "%" + param + "%");
    }
}
