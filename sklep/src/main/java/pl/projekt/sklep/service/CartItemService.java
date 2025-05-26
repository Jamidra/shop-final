package pl.projekt.sklep.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.projekt.sklep.exception.ResourceNotFoundException;
import pl.projekt.sklep.model.Cart;
import pl.projekt.sklep.model.CartItem;
import pl.projekt.sklep.model.Item;
import pl.projekt.sklep.repository.CartItemRepository;
import pl.projekt.sklep.repository.CartRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartItemService implements CartItemServiceInterface {
    private final CartRepository cartRepository;
    private final ItemServiceInterface itemService;
    private final CartServiceInterface cartService;



    @Transactional
    @Override
    public void addItemToCart(Long cartId, Long itemId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        Cart cart = cartService.getCart(cartId);
        Item item = itemService.getItemById(itemId);

        if (item.getPrice() == null) {
            throw new IllegalStateException("Item price cannot be null");
        }

        CartItem cartItem = cart.getItems()
                .stream()
                .filter(cartItem1 -> cartItem1.getItem().getItemId().equals(itemId))
                .findFirst()
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setCart(cart);
                    newItem.setItem(item);
                    newItem.setPrice(item.getPrice());
                    cart.getItems().add(newItem); // Add to cart's items set
                    return newItem;
                });

        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItem.setTotalPrice();

        // Update cart's total amount
        cart.setTotalAmount(cart.getItems()
                .stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void removeItemFromCart(Long cartId, Long itemId) throws ResourceNotFoundException {
        Cart cart = cartService.getCart(cartId);
        CartItem itemToRemove = cart.getItems()
                .stream()
                .filter(item -> item.getItem().getItemId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item not found in cart"));

        cart.getItems().remove(itemToRemove);

        // Update cart's total amount
        cart.setTotalAmount(cart.getItems()
                .stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        cartRepository.save(cart); // Cascade handles orphan removal
    }

    @Transactional
    @Override
    public void updateItemQuantity(Long cartId, Long itemId, int quantity) throws ResourceNotFoundException {
        try {
            Cart cart = cartService.getCart(cartId);
            cart.getItems()
                    .stream()
                    .filter(item -> item.getItem().getItemId().equals(itemId))
                    .findFirst()
                    .ifPresent(item -> {
                        item.setQuantity(quantity);
                        item.setPrice(item.getItem().getPrice());
                        item.setTotalPrice();
                    });
            BigDecimal totalAmount = cart.getItems()
                    .stream()
                    .map(CartItem::getTotalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            cart.setTotalAmount(totalAmount);
            cartRepository.save(cart);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Failed to update item quantity: " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public CartItem getCartItem(Long cartId, Long itemId) throws ResourceNotFoundException {
        Cart cart = cartService.getCart(cartId);
        return cart.getItems()
                .stream()
                .filter(item -> item.getItem().getItemId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item not found in cart"));
    }

    @Transactional
    @Override
    public void addItemAndInitialize(Long cartId, Long itemId, Integer quantity) {
        try {
            if (cartId == null) {
                cartId = cartService.initializeNewCart();
            }
            addItemToCart(cartId, itemId, quantity);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

}