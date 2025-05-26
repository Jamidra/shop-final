package pl.projekt.sklep.service;

import pl.projekt.sklep.dto.CartDto;
import pl.projekt.sklep.model.Cart;

import java.math.BigDecimal;

public interface CartServiceInterface {
    Cart getCart(Long cartId);
    CartDto getCartDto(Long cartId);
    void clearCart(Long cartId);
    BigDecimal getTotalPrice(Long cartId);
    Long initializeNewCart();
    Cart getCartByCartId(Long cartId);
}