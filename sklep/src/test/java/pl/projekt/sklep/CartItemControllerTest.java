package pl.projekt.sklep;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.projekt.sklep.controller.CartItemController;
import pl.projekt.sklep.dto.AddItemToCartRequest;
import pl.projekt.sklep.dto.UpdateCartItemQuantityDto;
import pl.projekt.sklep.exception.ResourceNotFoundException;
import pl.projekt.sklep.service.CartItemServiceInterface;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartItemControllerTest {

    @Mock
    private CartItemServiceInterface cartItemService;

    @InjectMocks
    private CartItemController cartItemController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addItemToCart_ValidRequest_ReturnsSuccessMessage() {
        AddItemToCartRequest request = new AddItemToCartRequest(1L, "TestItem", 3);
        doNothing().when(cartItemService).addItemAndInitialize(request.getCartId(), request.getItemName(), request.getQuantity());

        String result = cartItemController.addItemToCart(request);

        assertEquals("Added successfully", result);
        verify(cartItemService, times(1)).addItemAndInitialize(request.getCartId(), request.getItemName(), request.getQuantity());
    }

    @Test
    void addItemToCart_NullCartId_InitializesNewCart() {
        AddItemToCartRequest request = new AddItemToCartRequest(null, "TestItem", 3);
        doNothing().when(cartItemService).addItemAndInitialize(null, request.getItemName(), request.getQuantity());

        String result = cartItemController.addItemToCart(request);

        assertEquals("Added successfully", result);
        verify(cartItemService, times(1)).addItemAndInitialize(null, request.getItemName(), request.getQuantity());
    }

    @Test
    void addItemToCart_NonExistentCartOrItem_ThrowsResourceNotFoundException() {
        AddItemToCartRequest request = new AddItemToCartRequest(999L, "TestItem", 3);
        doThrow(new ResourceNotFoundException("Cart or item not found")).when(cartItemService).addItemAndInitialize(request.getCartId(), request.getItemName(), request.getQuantity());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> cartItemController.addItemToCart(request));
        assertEquals("Cart or item not found", exception.getMessage());
        verify(cartItemService, times(1)).addItemAndInitialize(request.getCartId(), request.getItemName(), request.getQuantity());
    }

    @Test
    void addItemToCart_NonPositiveQuantity_ThrowsIllegalArgumentException() {
        AddItemToCartRequest request = new AddItemToCartRequest(1L, "TestItem", 0);
        doThrow(new IllegalArgumentException("Quantity must be positive")).when(cartItemService).addItemAndInitialize(request.getCartId(), request.getItemName(), request.getQuantity());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cartItemController.addItemToCart(request));
        assertEquals("Quantity must be positive", exception.getMessage());
        verify(cartItemService, times(1)).addItemAndInitialize(request.getCartId(), request.getItemName(), request.getQuantity());
    }

    @Test
    void addItemToCart_NullItemPrice_ThrowsIllegalStateException() {
        AddItemToCartRequest request = new AddItemToCartRequest(1L, "TestItem", 3);
        doThrow(new IllegalStateException("Item price cannot be null")).when(cartItemService).addItemAndInitialize(request.getCartId(), request.getItemName(), request.getQuantity());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> cartItemController.addItemToCart(request));
        assertEquals("Item price cannot be null", exception.getMessage());
        verify(cartItemService, times(1)).addItemAndInitialize(request.getCartId(), request.getItemName(), request.getQuantity());
    }

    @Test
    void removeItemFromCart_ValidIds_ReturnsSuccessMessage() {
        Long cartId = 1L;
        String name = "TestItem";
        doNothing().when(cartItemService).removeItemFromCart(cartId, name);

        String result = cartItemController.removeItemFromCart(cartId, name);

        assertEquals("Item removed successfully", result);
        verify(cartItemService, times(1)).removeItemFromCart(cartId, name);
    }

    @Test
    void removeItemFromCart_NonExistentCartOrItem_ThrowsResourceNotFoundException() {
        Long cartId = 999L;
        String name = "TestItem";
        doThrow(new ResourceNotFoundException("Item not found in cart")).when(cartItemService).removeItemFromCart(cartId, name);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> cartItemController.removeItemFromCart(cartId, name));
        assertEquals("Item not found in cart", exception.getMessage());
        verify(cartItemService, times(1)).removeItemFromCart(cartId, name);
    }

    @Test
    void updateItemQuantity_ValidRequest_ReturnsSuccessMessage() {
        UpdateCartItemQuantityDto request = new UpdateCartItemQuantityDto(1L, "TestItem", 5);
        doNothing().when(cartItemService).updateItemQuantity(request.getCartId(), request.getItemName(), request.getQuantity());

        String result = cartItemController.updateItemQuantity(request);

        assertEquals("Updated successfully", result);
        verify(cartItemService, times(1)).updateItemQuantity(request.getCartId(), request.getItemName(), request.getQuantity());
    }

    @Test
    void updateItemQuantity_NonExistentCartOrItem_ThrowsResourceNotFoundException() {
        UpdateCartItemQuantityDto request = new UpdateCartItemQuantityDto(999L, "TestItem", 5);
        doThrow(new ResourceNotFoundException("Item not found in cart")).when(cartItemService).updateItemQuantity(request.getCartId(), request.getItemName(), request.getQuantity());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> cartItemController.updateItemQuantity(request));
        assertEquals("Item not found in cart", exception.getMessage());
        verify(cartItemService, times(1)).updateItemQuantity(request.getCartId(), request.getItemName(), request.getQuantity());
    }
}