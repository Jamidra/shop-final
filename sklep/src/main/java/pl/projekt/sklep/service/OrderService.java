package pl.projekt.sklep.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.projekt.sklep.dto.OrderDto;
import pl.projekt.sklep.exception.ResourceNotFoundException;
import pl.projekt.sklep.mapper.OrderMapper;
import pl.projekt.sklep.model.*;
import pl.projekt.sklep.repository.ItemRepository;
import pl.projekt.sklep.repository.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Service
public class OrderService implements OrderServiceInterface {
    private final OrderRepository orderRepository;
    private final ItemRepository productRepository;
    private final CartService cartService;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, ItemRepository productRepository, CartService cartService, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.cartService = cartService;
        this.orderMapper = orderMapper;
    }

    @Transactional
    @Override
    public HashMap<String, Object> createOrder(Long cartId) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            Cart cart = cartService.getCartByCartId(cartId);
            Order order = createOrder();
            List<OrderItem> orderItemList = createOrderItems(order, cart);
            order.setOrderItems(new HashSet<>(orderItemList));
            order.setTotalAmount(calculateTotalAmount(orderItemList));
            Order savedOrder = orderRepository.save(order);
            cartService.clearCart(cart.getCartId());
            OrderDto orderDto = orderMapper.toDto(savedOrder);
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

    private Order createOrder() {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());
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
    public HashMap<String, Object> getOrder(Long orderId) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            OrderDto order = orderRepository.findById(orderId)
                    .map(orderMapper::toDto)
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
            response.put("status", "success");
            response.put("data", order);
            response.put("statusCode", 200);
        } catch (ResourceNotFoundException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("statusCode", 404);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Internal server error");
            response.put("statusCode", 500);
        }
        return response;
    }

    @Transactional
    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .toList();
    }
}