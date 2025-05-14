package mate.academy.bookshop.mapper;

import mate.academy.bookshop.config.MapperConfig;
import mate.academy.bookshop.dto.order.CreateOrderRequestDto;
import mate.academy.bookshop.dto.order.OrderDto;
import mate.academy.bookshop.model.Order;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    OrderDto toDto(Order order);

    Order toModel(CreateOrderRequestDto requestDto);
}
