package pl.projekt.sklep;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import pl.projekt.sklep.controller.CartController;
import pl.projekt.sklep.dto.CartDto;
import pl.projekt.sklep.service.CartServiceInterface;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartControllerTest {

    @Mock
    private CartServiceInterface cartService;

    @InjectMocks
    private CartController cartController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCart_ValidCartId_ReturnsCartDto() {
        // Arrange
        Long cartId = 1L;
        CartDto expectedCartDto = new CartDto();
        when(cartService.getCartDto(cartId)).thenReturn(expectedCartDto);

        // Act
        CartDto result = cartController.getCart(cartId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedCartDto, result);
        verify(cartService, times(1)).getCartDto(cartId);
    }

    @Test
    void getCart_NonExistentCartId_ThrowsNotFoundException() {
        // Arrange
        Long cartId = 999L;
        when(cartService.getCartDto(cartId)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found"));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> cartController.getCart(cartId));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Cart not found", exception.getReason());
        verify(cartService, times(1)).getCartDto(cartId);
    }

    @Test
    void clearCart_ValidCartId_ReturnsSuccessMessage() {
        // Arrange
        Long cartId = 1L;
        doNothing().when(cartService).clearCart(cartId);

        // Act
        String result = cartController.clearCart(cartId);

        // Assert
        assertEquals("Cart cleared successfully", result);
        verify(cartService, times(1)).clearCart(cartId);
    }

    @Test
    void clearCart_NonExistentCartId_ThrowsNotFoundException() {
        // Arrange
        Long cartId = 999L;
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found")).when(cartService).clearCart(cartId);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> cartController.clearCart(cartId));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Cart not found", exception.getReason());
        verify(cartService, times(1)).clearCart(cartId);
    }

    @Test
    void getTotalAmount_ValidCartId_ReturnsTotalPrice() {
        // Arrange
        Long cartId = 1L;
        BigDecimal expectedTotal = new BigDecimal("99.99");
        when(cartService.getTotalPrice(cartId)).thenReturn(expectedTotal);

        // Act
        BigDecimal result = cartController.getTotalAmount(cartId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedTotal, result);
        verify(cartService, times(1)).getTotalPrice(cartId);
    }

    @Test
    void getTotalAmount_NonExistentCartId_ThrowsNotFoundException() {
        // Arrange
        Long cartId = 999L;
        when(cartService.getTotalPrice(cartId)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found"));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> cartController.getTotalAmount(cartId));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Cart not found", exception.getReason());
        verify(cartService, times(1)).getTotalPrice(cartId);
    }
}
