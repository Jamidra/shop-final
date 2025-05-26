package pl.projekt.sklep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.projekt.sklep.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
