package pl.projekt.sklep.service;

import jakarta.transaction.Transactional;
import pl.projekt.sklep.exception.ResourceNotFoundException;
import pl.projekt.sklep.model.CartItem;

public interface CartItemServiceInterface {
    @Transactional
    void addItemToCart(Long cartId, Long itemId, int quantity) throws ResourceNotFoundException;
    void removeItemFromCart(Long cartId, Long itemId) throws ResourceNotFoundException;
    void updateItemQuantity(Long cartId, Long itemId, int quantity) throws ResourceNotFoundException;
    CartItem getCartItem(Long cartId, Long itemId) throws ResourceNotFoundException;
    void addItemAndInitialize(Long cartId, Long itemId, Integer quantity);
}