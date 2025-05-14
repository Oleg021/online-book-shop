package mate.academy.bookshop.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import mate.academy.bookshop.model.Order;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    private Set<OrderItemDto> items = new HashSet<>();
    private LocalDateTime orderDate;
    private BigDecimal total;
    private Order.Status status;
}
