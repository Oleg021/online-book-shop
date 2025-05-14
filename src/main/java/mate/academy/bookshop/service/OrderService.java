package mate.academy.bookshop.service;

import jakarta.validation.Valid;
import mate.academy.bookshop.dto.order.CreateOrderRequestDto;
import mate.academy.bookshop.dto.order.OrderDto;
import mate.academy.bookshop.dto.order.OrderItemDto;
import mate.academy.bookshop.dto.order.UpdateOrderStatusDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Page<OrderDto> getAll(Long userId, Pageable pageable);

    OrderDto create(Long userId, @Valid CreateOrderRequestDto address);

    OrderDto updateStatus(Long orderId, @Valid UpdateOrderStatusDto status);

    Page<OrderItemDto> getAllItems(Long userId, Long orderId, Pageable pageable);

    OrderItemDto getItemById(Long userId, Long orderId, Long itemId);
}
