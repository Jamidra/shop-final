package pl.projekt.sklep;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pl.projekt.sklep.controller.OrderController;
import pl.projekt.sklep.service.OrderServiceInterface;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @Mock
    private OrderServiceInterface orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrder_ValidCartId_ReturnsOrderJson() {
        // Arrange
        Long cartId = 1L;
        HashMap<String, Object> orderMap = new HashMap<>();
        orderMap.put("statusCode", 200);
        orderMap.put("message", "Order created");
        when(orderService.createOrder(cartId)).thenReturn(orderMap);

        // Act
        String result = orderController.createOrder(cartId);

        // Assert
        assertEquals(orderMap.toString(), result);
        verify(orderService, times(1)).createOrder(cartId);
    }

    @Test
    void createOrder_InvalidCartId_ReturnsErrorJson() {
        // Arrange
        Long cartId = 999L;
        HashMap<String, Object> errorMap = new HashMap<>();
        errorMap.put("statusCode", 500);
        errorMap.put("message", "Internal server error");
        when(orderService.createOrder(cartId)).thenReturn(errorMap);

        // Act
        String result = orderController.createOrder(cartId);

        // Assert
        assertEquals(errorMap.toString(), result);
        verify(orderService, times(1)).createOrder(cartId);
    }

    @Test
    void getOrderByOrderId_ValidId_ReturnsOrderJson() {
        // Arrange
        Long orderId = 1L;
        String orderJson = "{\"statusCode\": 200}";
        when(orderService.getOrder(orderId)).thenReturn(orderJson);

        // Act
        String result = orderController.getOrderByOrderId(orderId);

        // Assert
        assertEquals(orderJson, result);
        verify(orderService, times(1)).getOrder(orderId);
    }

    @Test
    void getOrderByOrderId_NonExistentId_ReturnsNotFoundJson() {
        // Arrange
        Long orderId = 999L;
        String errorJson = "{\"statusCode\": 404}";
        when(orderService.getOrder(orderId)).thenReturn(errorJson);

        // Act
        String result = orderController.getOrderByOrderId(orderId);

        // Assert
        assertEquals(errorJson, result);
        verify(orderService, times(1)).getOrder(orderId);
    }

    @Test
    void getAllOrders_ReturnsOrdersJson() {
        // Arrange
        String ordersJson = "{\"statusCode\": 200}";
        when(orderService.getAllOrders()).thenReturn(ordersJson);

        // Act
        String result = orderController.getAllOrders();

        // Assert
        assertEquals(ordersJson, result);
        verify(orderService, times(1)).getAllOrders();
    }
}
