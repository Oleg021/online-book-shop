package mate.academy.bookshop.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class IsbnValidator implements ConstraintValidator<Isbn, String> {
    private static final String ISBN_REGEX = "^(\\d{9}[\\dX]|\\d{13})$";

    @Override
    public boolean isValid(String isbn,
                           ConstraintValidatorContext constraintValidatorContext) {
        return isbn != null && Pattern.compile(ISBN_REGEX).matcher(isbn).matches();
    }
}
