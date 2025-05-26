package pl.projekt.sklep.service;

import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.projekt.sklep.dto.CartDto;
import pl.projekt.sklep.exception.ResourceNotFoundException;
import pl.projekt.sklep.mapper.CartMapper;
import pl.projekt.sklep.model.Cart;
import pl.projekt.sklep.model.CartItem;
import pl.projekt.sklep.repository.CartItemRepository;
import pl.projekt.sklep.repository.CartRepository;

import java.math.BigDecimal;


@RequiredArgsConstructor
@Service
public class CartService implements CartServiceInterface {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartMapper cartMapper;

    @Transactional
    @Override
    public Cart getCart(Long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
    }

    @Transactional
    @Override
    public CartDto getCartDto(Long cartId) {
        Cart cart = getCart(cartId);
        return cartMapper.toDto(cart);
    }

    @Transactional
    @Override
    public void clearCart(Long cartId) {
        Cart cart = getCart(cartId);
        cartItemRepository.deleteAllById(cartId);
        cart.getItems().clear();
        cartRepository.deleteById(cartId);
    }

    @Override
    public BigDecimal getTotalPrice(Long cartId) {
        Cart cart = getCart(cartId);
        return cart.getTotalAmount();
    }


    @Override
    public Long initializeNewCart() {
        int retries = 3;
        while (retries > 0) {
            try {
                Cart newCart = new Cart();
                return cartRepository.save(newCart).getCartId();
            } catch (ObjectOptimisticLockingFailureException e) {
                retries--;
                if (retries == 0) {
                    throw new RuntimeException("Failed to create cart after retries", e);
                }
            }
        }
        throw new RuntimeException("Failed to create cart");
    }

    @Override
    public Cart getCartByCartId(Long cartId) {
        return cartRepository.findById(cartId)
                .orElse(null); // Or throw exception if null is not acceptable
    }
    @Transactional
    public void addItem(Long cartId, CartItem item) {
        Cart cart = getCart(cartId);
        cart.getItems().add(item);
        item.setCart(cart);
        updateTotalAmount(cart);
        cartRepository.save(cart);
    }

    @Transactional
    public void removeItem(Long cartId, CartItem item) {
        Cart cart = getCart(cartId);
        cart.getItems().remove(item);
        item.setCart(null);
        updateTotalAmount(cart);
        cartRepository.save(cart);
    }

    private void updateTotalAmount(Cart cart) {
        cart.setTotalAmount(cart.getItems().stream().map(item -> {
            BigDecimal unitPrice = item.getPrice();
            if (unitPrice == null) {
                return BigDecimal.ZERO;
            }
            return unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
        }).reduce(BigDecimal.ZERO, BigDecimal::add));
    }
}