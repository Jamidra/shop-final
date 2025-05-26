package pl.projekt.sklep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.projekt.sklep.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteAllById(Long id);
}
