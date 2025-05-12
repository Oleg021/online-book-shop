package mate.academy.bookshop.dto.cart;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateCartItemQuantityDto {
    @Positive
    private int quantity;
}
