package mate.academy.bookshop.dto.cart;

import java.util.Set;
import lombok.Data;
import mate.academy.bookshop.model.CartItem;

@Data
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private Set<CartItem> cartItems;
}
