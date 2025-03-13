package mate.academy.bookshop.repository.book;

import lombok.RequiredArgsConstructor;
import mate.academy.bookshop.dto.BookSearchParameters;
import mate.academy.bookshop.model.Book;
import mate.academy.bookshop.repository.SpecificationBuilder;
import mate.academy.bookshop.repository.SpecificationProviderManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    public static final String AUTHOR = "author";
    public static final String TITLE = "title";
    private SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParameters searchParameters) {
        Specification<Book> specification = Specification.where(null);
        if (searchParameters.author() != null && searchParameters.author().length() > 0) {
            specification.and(bookSpecificationProviderManager.getSpecificationProvider(AUTHOR)
                    .getSpecification(searchParameters.author()));
        }
        if (searchParameters.titlePart() != null && searchParameters.titlePart().length() > 0) {
            specification.and(bookSpecificationProviderManager.getSpecificationProvider(TITLE)
                    .getSpecification(searchParameters.titlePart()));
        }
        return specification;
    }
}
