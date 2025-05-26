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
        // Arrange
        AddItemToCartRequest request = new AddItemToCartRequest(1L, 2L, 3);
        doNothing().when(cartItemService).addItemAndInitialize(request.getCartId(), request.getItemId(), request.getQuantity());

        // Act
        String result = cartItemController.addItemToCart(request);

        // Assert
        assertEquals("Added successfully", result);
        verify(cartItemService, times(1)).addItemAndInitialize(request.getCartId(), request.getItemId(), request.getQuantity());
    }

    @Test
    void addItemToCart_NullCartId_InitializesNewCart() {
        // Arrange
        AddItemToCartRequest request = new AddItemToCartRequest(null, 2L, 3);
        doNothing().when(cartItemService).addItemAndInitialize(null, request.getItemId(), request.getQuantity());

        // Act
        String result = cartItemController.addItemToCart(request);

        // Assert
        assertEquals("Added successfully", result);
        verify(cartItemService, times(1)).addItemAndInitialize(null, request.getItemId(), request.getQuantity());
    }

    @Test
    void addItemToCart_NonExistentCartOrItem_ThrowsResourceNotFoundException() {
        // Arrange
        AddItemToCartRequest request = new AddItemToCartRequest(999L, 2L, 3);
        doThrow(new ResourceNotFoundException("Cart or item not found")).when(cartItemService).addItemAndInitialize(request.getCartId(), request.getItemId(), request.getQuantity());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> cartItemController.addItemToCart(request));
        assertEquals("Cart or item not found", exception.getMessage());
        verify(cartItemService, times(1)).addItemAndInitialize(request.getCartId(), request.getItemId(), request.getQuantity());
    }

    @Test
    void addItemToCart_NonPositiveQuantity_ThrowsIllegalArgumentException() {
        // Arrange
        AddItemToCartRequest request = new AddItemToCartRequest(1L, 2L, 0);
        doThrow(new IllegalArgumentException("Quantity must be positive")).when(cartItemService).addItemAndInitialize(request.getCartId(), request.getItemId(), request.getQuantity());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cartItemController.addItemToCart(request));
        assertEquals("Quantity must be positive", exception.getMessage());
        verify(cartItemService, times(1)).addItemAndInitialize(request.getCartId(), request.getItemId(), request.getQuantity());
    }

    @Test
    void addItemToCart_NullItemPrice_ThrowsIllegalStateException() {
        // Arrange
        AddItemToCartRequest request = new AddItemToCartRequest(1L, 2L, 3);
        doThrow(new IllegalStateException("Item price cannot be null")).when(cartItemService).addItemAndInitialize(request.getCartId(), request.getItemId(), request.getQuantity());

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> cartItemController.addItemToCart(request));
        assertEquals("Item price cannot be null", exception.getMessage());
        verify(cartItemService, times(1)).addItemAndInitialize(request.getCartId(), request.getItemId(), request.getQuantity());
    }

    @Test
    void removeItemFromCart_ValidIds_ReturnsSuccessMessage() {
        // Arrange
        Long cartId = 1L;
        Long itemId = 2L;
        doNothing().when(cartItemService).removeItemFromCart(cartId, itemId);

        // Act
        String result = cartItemController.removeItemFromCart(cartId, itemId);

        // Assert
        assertEquals("Item removed successfully", result);
        verify(cartItemService, times(1)).removeItemFromCart(cartId, itemId);
    }

    @Test
    void removeItemFromCart_NonExistentCartOrItem_ThrowsResourceNotFoundException() {
        // Arrange
        Long cartId = 999L;
        Long itemId = 2L;
        doThrow(new ResourceNotFoundException("Item not found in cart")).when(cartItemService).removeItemFromCart(cartId, itemId);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> cartItemController.removeItemFromCart(cartId, itemId));
        assertEquals("Item not found in cart", exception.getMessage());
        verify(cartItemService, times(1)).removeItemFromCart(cartId, itemId);
    }

    @Test
    void updateItemQuantity_ValidRequest_ReturnsSuccessMessage() {
        // Arrange
        UpdateCartItemQuantityDto request = new UpdateCartItemQuantityDto(1L, 2L, 5);
        doNothing().when(cartItemService).updateItemQuantity(request.getCartId(), request.getItemId(), request.getQuantity());

        // Act
        String result = cartItemController.updateItemQuantity(request);

        // Assert
        assertEquals("Updated successfully", result);
        verify(cartItemService, times(1)).updateItemQuantity(request.getCartId(), request.getItemId(), request.getQuantity());
    }

    @Test
    void updateItemQuantity_NonExistentCartOrItem_ThrowsResourceNotFoundException() {
        // Arrange
        UpdateCartItemQuantityDto request = new UpdateCartItemQuantityDto(999L, 2L, 5);
        doThrow(new ResourceNotFoundException("Item not found in cart")).when(cartItemService).updateItemQuantity(request.getCartId(), request.getItemId(), request.getQuantity());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> cartItemController.updateItemQuantity(request));
        assertEquals("Item not found in cart", exception.getMessage());
        verify(cartItemService, times(1)).updateItemQuantity(request.getCartId(), request.getItemId(), request.getQuantity());
    }
}
