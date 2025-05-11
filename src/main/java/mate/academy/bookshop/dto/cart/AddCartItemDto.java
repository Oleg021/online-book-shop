package mate.academy.bookshop.dto.cart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AddCartItemDto {
    @NotNull
    @Positive
    private Long bookId;
    @Positive
    private int quantity;
}
