package pl.projekt.sklep.service;

import jakarta.transaction.Transactional;
import pl.projekt.sklep.exception.ResourceNotFoundException;

public interface CartItemServiceInterface {
    @Transactional
    void addItemToCart(Long cartId, String name, int quantity) throws ResourceNotFoundException;
    void removeItemFromCart(Long cartId, String name) throws ResourceNotFoundException;
    void updateItemQuantity(Long cartId, String name, int quantity) throws ResourceNotFoundException;
    void addItemAndInitialize(Long cartId, String name, Integer quantity);
}