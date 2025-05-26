package pl.projekt.sklep.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.projekt.sklep.dto.OrderDto;
import pl.projekt.sklep.dto.OrderItemDto;
import pl.projekt.sklep.exception.ResourceNotFoundException;
import pl.projekt.sklep.model.*;
import pl.projekt.sklep.repository.ItemRepository;
import pl.projekt.sklep.repository.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService implements OrderServiceInterface {
    private final OrderRepository orderRepository;
    private final ItemRepository productRepository;
    private final CartService cartService;
    private final ObjectMapper objectMapper;

    public OrderService(OrderRepository orderRepository, ItemRepository productRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.cartService = cartService;
        this.objectMapper = new ObjectMapper();
    }

    @Transactional
    @Override
    public HashMap<String, Object> createOrder(Long cartId) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            Cart cart = cartService.getCartByCartId(cartId);
            Order order = createOrder(cart);
            List<OrderItem> orderItemList = createOrderItems(order, cart);
            order.setOrderItems(new HashSet<>(orderItemList));
            order.setTotalAmount(calculateTotalAmount(orderItemList));
            Order savedOrder = orderRepository.save(order);
            cartService.clearCart(cart.getCartId());
            OrderDto orderDto = convertToDto(savedOrder);
            response.put("status", "success");
            response.put("message", "Order created successfully");
            response.put("data", orderDto);
            response.put("statusCode", 200);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("statusCode", 500);
        }
        return response;
    }

    private Order createOrder(Cart cart) {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart) {
        return cart.getItems().stream().map(cartItem -> {
            Item product = cartItem.getItem();
            product.setInventory(product.getInventory() - cartItem.getQuantity());
            productRepository.save(product);
            return new OrderItem(
                    order,
                    product,
                    cartItem.getQuantity(),
                    cartItem.getPrice());
        }).toList();
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList) {
        return orderItemList
                .stream()
                .map(item -> item.getPrice()
                        .multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional
    @Override
    public String getOrder(Long orderId) {
        try {
            OrderDto order = orderRepository.findById(orderId)
                    .map(this::convertToDto)
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("data", order);
            response.put("statusCode", 200);
            return objectMapper.writeValueAsString(response);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("statusCode", 404);
            try {
                return objectMapper.writeValueAsString(response);
            } catch (JsonProcessingException jpe) {
                return "{\"status\":\"error\",\"message\":\"Serialization error\",\"statusCode\":500}";
            }
        } catch (JsonProcessingException jpe) {
            return "{\"status\":\"error\",\"message\":\"Serialization error\",\"statusCode\":500}";
        }
    }

    @Transactional
    @Override
    public String getAllOrders() {
        try {
            List<OrderDto> items = orderRepository.findAll().stream()
                    .peek(order -> Hibernate.initialize(order.getOrderItems()))
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("data", items);
            response.put("statusCode", 200);
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            return "{\"status\":\"error\",\"message\":\"Serialization error\",\"statusCode\":500}";
        }
    }

    private OrderDto convertToDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(order.getOrderId());
        orderDto.setOrderDate(order.getOrderDate().atStartOfDay());
        orderDto.setTotalAmount(order.getTotalAmount());
        orderDto.setStatus(order.getOrderStatus().toString());
        orderDto.setItems(order.getOrderItems().stream().map(this::convertToOrderItemDto).collect(Collectors.toList()));
        return orderDto;
    }

    private OrderItemDto convertToOrderItemDto(OrderItem orderItem) {
        OrderItemDto itemDto = new OrderItemDto();
        itemDto.setItemId(orderItem.getId());
        itemDto.setProductName(orderItem.getItem().getName());
        itemDto.setQuantity(orderItem.getQuantity());
        itemDto.setPrice(orderItem.getPrice());
        return itemDto;
    }
}