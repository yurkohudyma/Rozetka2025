package ua.hudyma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.hudyma.domain.orders.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
