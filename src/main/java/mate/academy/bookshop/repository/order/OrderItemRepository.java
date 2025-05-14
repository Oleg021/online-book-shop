package mate.academy.bookshop.repository.order;

import mate.academy.bookshop.model.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository
        extends JpaRepository<OrderItem, Long> {
    Page<OrderItem> findAllByOrderUserIdAndOrderId(Long userId, Long orderId, Pageable pageable);

    OrderItem findByOrderUserIdAndOrderIdAndId(Long userId, Long orderId, Long orderItemId);
}
