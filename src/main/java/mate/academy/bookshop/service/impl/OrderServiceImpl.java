package mate.academy.bookshop.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.bookshop.dto.order.CreateOrderRequestDto;
import mate.academy.bookshop.dto.order.OrderDto;
import mate.academy.bookshop.dto.order.OrderItemDto;
import mate.academy.bookshop.dto.order.UpdateOrderStatusDto;
import mate.academy.bookshop.exceptions.EntityNotFoundException;
import mate.academy.bookshop.exceptions.OrderProcessingException;
import mate.academy.bookshop.mapper.OrderItemMapper;
import mate.academy.bookshop.mapper.OrderMapper;
import mate.academy.bookshop.model.CartItem;
import mate.academy.bookshop.model.Order;
import mate.academy.bookshop.model.OrderItem;
import mate.academy.bookshop.repository.cart.ShoppingCartRepository;
import mate.academy.bookshop.repository.order.OrderItemRepository;
import mate.academy.bookshop.repository.order.OrderRepository;
import mate.academy.bookshop.repository.user.UserRepository;
import mate.academy.bookshop.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final UserRepository userRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public Page<OrderDto> getAll(Long userId, Pageable pageable) {
        return orderRepository
                .findAllByUserId(userId, pageable)
                .map(orderMapper::toDto);
    }

    @Override
    public OrderDto create(Long userId, CreateOrderRequestDto address) {
        Order order = orderMapper.toModel(address);
        order.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                                "Can`t find user by id " + userId))
        );
        order.setStatus(Order.Status.PENDING);
        Set<CartItem> cartItems = shoppingCartRepository
                        .findById(userId)
                        .get().getCarItems();
        if (cartItems.isEmpty()) {
            throw new OrderProcessingException(
                    "Shopping cart is empty for user " + userId
            );
        }
        Set<OrderItem> orderItems = cartItems.stream()
                .map(orderItemMapper::fromCartToOrderItem)
                .collect(Collectors.toSet());
        order.setOrderItems(orderItems);
        BigDecimal totalPrice = orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(totalPrice);
        order.setOrderDate(LocalDateTime.now());
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto updateStatus(Long orderId, UpdateOrderStatusDto status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can`t find order by id " + orderId)
                );
        order.setStatus(status.getStatus());
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public Page<OrderItemDto> getAllItems(Long userId, Long orderId, Pageable pageable) {
        return orderItemRepository
                .findAllByOrderUserIdAndOrderId(userId, orderId, pageable)
                .map(orderItemMapper::toDto);
    }

    @Override
    public OrderItemDto getItemById(Long userId, Long orderId, Long itemId) {
        return orderItemMapper.toDto(orderItemRepository
                .findByOrderUserIdAndOrderIdAndId(userId, orderId, itemId));
    }
}
