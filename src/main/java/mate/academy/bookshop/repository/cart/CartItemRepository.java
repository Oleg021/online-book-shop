package mate.academy.bookshop.repository.cart;

import java.util.Optional;
import mate.academy.bookshop.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByIdAndShoppingCartId(Long id, Long cartId);
}
