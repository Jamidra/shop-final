package pl.projekt.sklep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.projekt.sklep.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByCartId(Long cartId);
}
