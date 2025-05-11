package mate.academy.bookshop.service;

import mate.academy.bookshop.dto.cart.AddCartItemDto;
import mate.academy.bookshop.dto.cart.ShoppingCartDto;
import mate.academy.bookshop.dto.cart.UpdateCartItemQuantityDto;
import mate.academy.bookshop.model.User;

public interface ShoppingCartService {
    ShoppingCartDto addBook(Long userId, AddCartItemDto addCartItemDto);

    ShoppingCartDto getInfo(Long userId);

    ShoppingCartDto updateQuantity(
            Long cartItemId,
            Long userId,
            UpdateCartItemQuantityDto updateCartItemQuantityDto
    );

    void deleteBook(Long cartItemId, Long userId);

    void createShoppingCart(User user);
}
