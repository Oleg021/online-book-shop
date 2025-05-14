package mate.academy.bookshop.mapper;

import java.math.BigDecimal;
import mate.academy.bookshop.config.MapperConfig;
import mate.academy.bookshop.dto.order.OrderItemDto;
import mate.academy.bookshop.model.CartItem;
import mate.academy.bookshop.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, imports = BigDecimal.class)
public interface OrderItemMapper {
    OrderItemDto toDto(OrderItem orderItem);

    @Mapping(target = "order", ignore = true)
    @Mapping(target = "price", expression = "java(cartItem.getBook().getPrice() "
            + " .multiply(BigDecimal.valueOf(cartItem.getQuantity())))")
    OrderItem fromCartToOrderItem(CartItem cartItem);
}
