package mate.academy.bookshop.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.bookshop.dto.cart.AddCartItemDto;
import mate.academy.bookshop.dto.cart.ShoppingCartDto;
import mate.academy.bookshop.dto.cart.UpdateCartItemQuantityDto;
import mate.academy.bookshop.exceptions.EntityNotFoundException;
import mate.academy.bookshop.mapper.CartItemMapper;
import mate.academy.bookshop.mapper.ShoppingCartMapper;
import mate.academy.bookshop.model.CartItem;
import mate.academy.bookshop.model.ShoppingCart;
import mate.academy.bookshop.model.User;
import mate.academy.bookshop.repository.cart.CartItemRepository;
import mate.academy.bookshop.repository.cart.ShoppingCartRepository;
import mate.academy.bookshop.service.ShoppingCartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartMapper shoppingCartMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;

    @Override
    public ShoppingCartDto addBook(Long userId, AddCartItemDto addCartItemDto) {
        ShoppingCart shoppingCart =
                shoppingCartRepository.findById(userId).orElseThrow(
                        () -> new EntityNotFoundException("Can`t find shopping cart by id: "
                        + userId));
        CartItem cartItem = cartItemMapper.toEntity(addCartItemDto);
        for (CartItem item : shoppingCart.getCarItems()) {
            if (item.getBook().equals(cartItem.getBook())) {
                item.setQuantity(item.getQuantity() + addCartItemDto.getQuantity());
                return shoppingCartMapper.toDto(shoppingCart);
            }
        }
        cartItem.setShoppingCart(shoppingCart);
        shoppingCart.getCarItems().add(cartItem);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto getInfo(Long userId) {
        return shoppingCartMapper.toDto(
                shoppingCartRepository.findById(userId).orElseThrow(
                        () -> new EntityNotFoundException("Can't find shopping cart by id "
                        + userId))
        );
    }

    @Override
    public ShoppingCartDto updateQuantity(
            Long cartItemId,
            Long userId,
            UpdateCartItemQuantityDto updateQuantityDto) {
        ShoppingCart shoppingCart = shoppingCartRepository
                .findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can`t find shopping cart by id: " + userId)
                );
        CartItem cartItem = cartItemRepository
                .findByIdAndShoppingCartId(cartItemId, shoppingCart.getId())
                .orElseThrow(()
                        -> new EntityNotFoundException("Can`t find cart item"));
        cartItem.setQuantity(updateQuantityDto.getQuantity());
        shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public void deleteBook(Long cartItemId, Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository
                .findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can`t find shopping cart by id: " + userId)
                );
        CartItem cartItem = cartItemRepository
                .findByIdAndShoppingCartId(cartItemId, shoppingCart.getId())
                .orElseThrow(()
                        -> new EntityNotFoundException("Can`t find cart item"));
        cartItemRepository.delete(cartItem);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public void createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }
}
