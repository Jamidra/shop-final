package pl.projekt.sklep;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.projekt.sklep.controller.OrderController;
import pl.projekt.sklep.dto.OrderDto;
import pl.projekt.sklep.dto.OrderItemDto;
import pl.projekt.sklep.mapper.OrderMapper;
import pl.projekt.sklep.model.Order;
import pl.projekt.sklep.model.OrderStatus;
import pl.projekt.sklep.service.OrderServiceInterface;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @Mock
    private OrderServiceInterface orderService;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderController orderController;

    private OrderDto orderDto;
    private Order order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        orderDto = new OrderDto();
        orderDto.setOrderId(1L);
        orderDto.setOrderDate(LocalDateTime.now());
        orderDto.setTotalAmount(new BigDecimal("100"));
        orderDto.setStatus(OrderStatus.PENDING.name());

        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setItemId(1L);
        orderItemDto.setProductName("Product1");
        orderItemDto.setQuantity(2);
        orderItemDto.setPrice(new BigDecimal("50"));
        orderDto.setItems(List.of(orderItemDto));

        order = new Order();
        order.setOrderId(1L);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(new BigDecimal("100"));
        order.setOrderStatus(OrderStatus.PENDING);
    }

    @Test
    void createOrder_ValidCartId_ReturnsOrderResponse() {
        // Arrange
        Long cartId = 1L;
        HashMap<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Order created successfully");
        response.put("data", orderDto);
        response.put("statusCode", 200);
        when(orderService.createOrder(cartId)).thenReturn(response);

        // Act
        HashMap<String, Object> result = orderController.createOrder(cartId);

        // Assert
        assertEquals(response, result);
        assertEquals("success", result.get("status"));
        assertEquals("Order created successfully", result.get("message"));
        assertEquals(orderDto, result.get("data"));
        assertEquals(200, result.get("statusCode"));
        verify(orderService, times(1)).createOrder(cartId);
    }

    @Test
    void createOrder_InvalidCartId_ReturnsErrorResponse() {
        // Arrange
        Long cartId = 999L;
        HashMap<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "error");
        errorResponse.put("message", "Cart not found");
        errorResponse.put("statusCode", 500);
        when(orderService.createOrder(cartId)).thenReturn(errorResponse);

        // Act
        HashMap<String, Object> result = orderController.createOrder(cartId);

        // Assert
        assertEquals(errorResponse, result);
        assertEquals("error", result.get("status"));
        assertEquals("Cart not found", result.get("message"));
        assertEquals(500, result.get("statusCode"));
        verify(orderService, times(1)).createOrder(cartId);
    }

    @Test
    void getOrderByOrderId_ValidId_ReturnsOrderResponse() {
        // Arrange
        Long orderId = 1L;
        HashMap<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", orderDto);
        response.put("statusCode", 200);
        when(orderService.getOrder(orderId)).thenReturn(response);

        // Act
        HashMap<String, Object> result = orderController.getOrderByOrderId(orderId);

        // Assert
        assertEquals(response, result);
        assertEquals("success", result.get("status"));
        assertEquals(orderDto, result.get("data"));
        assertEquals(200, result.get("statusCode"));
        verify(orderService, times(1)).getOrder(orderId);
    }

    @Test
    void getOrderByOrderId_NonExistentId_ReturnsNotFoundResponse() {
        // Arrange
        Long orderId = 999L;
        HashMap<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "error");
        errorResponse.put("message", "Order not found");
        errorResponse.put("statusCode", 404);
        when(orderService.getOrder(orderId)).thenReturn(errorResponse);

        // Act
        HashMap<String, Object> result = orderController.getOrderByOrderId(orderId);

        // Assert
        assertEquals(errorResponse, result);
        assertEquals("error", result.get("status"));
        assertEquals("Order not found", result.get("message"));
        assertEquals(404, result.get("statusCode"));
        verify(orderService, times(1)).getOrder(orderId);
    }

    @Test
    void getAllOrders_ReturnsOrdersList() {
        // Arrange
        List<Order> orders = List.of(order);
        List<OrderDto> orderDtos = List.of(orderDto);
        when(orderService.getAllOrders()).thenReturn(orders);
        when(orderMapper.toDto(order)).thenReturn(orderDto);

        // Act
        List<OrderDto> result = orderController.getAllOrders();

        // Assert
        assertEquals(orderDtos, result);
        assertEquals(1, result.size());
        assertEquals(orderDto, result.get(0));
        verify(orderService, times(1)).getAllOrders();
        verify(orderMapper, times(1)).toDto(order);
    }
}
