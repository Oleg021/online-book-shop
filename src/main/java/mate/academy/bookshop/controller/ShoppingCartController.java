package mate.academy.bookshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.bookshop.dto.cart.AddCartItemDto;
import mate.academy.bookshop.dto.cart.ShoppingCartDto;
import mate.academy.bookshop.dto.cart.UpdateCartItemQuantityDto;
import mate.academy.bookshop.service.ShoppingCartService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management",
        description = "Endpoints for managing shopping cart")
@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class ShoppingCartController implements UserContextHelper {
    private final ShoppingCartService shoppingCartService;

    @Operation(summary = "Add book",
            description = "Add book to cart")
    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ShoppingCartDto addBook(@Valid @RequestBody AddCartItemDto addCartItemDto,
                                   Authentication authentication) {
        return shoppingCartService.addBook(getUserId(authentication), addCartItemDto);
    }

    @Operation(summary = "Return cart info",
            description = "Get info about cart")
    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ShoppingCartDto getInfo(Authentication authentication) {
        return shoppingCartService.getInfo(getUserId(authentication));
    }

    @Operation(summary = "Update quantity of books",
            description = "Update quantity of books and return cart info")
    @PutMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ShoppingCartDto updateQuantity(@PathVariable Long cartItemId,
                                          @Valid @RequestBody
                                          UpdateCartItemQuantityDto updateQuantityDto,
                                          Authentication authentication) {
        return shoppingCartService
                .updateQuantity(getUserId(authentication),
                        cartItemId,
                        updateQuantityDto);
    }

    @Operation(summary = "Delete book from cart",
            description = "Delete book from cart and return cart info")
    @DeleteMapping("/items/{cartItemId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void deleteBook(
            @PathVariable Long cartItemId,
            Authentication authentication) {
        shoppingCartService.deleteBook(getUserId(authentication), cartItemId);
    }
}
