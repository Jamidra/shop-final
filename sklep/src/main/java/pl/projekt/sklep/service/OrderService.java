package pl.projekt.sklep.service;

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
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService implements OrderServiceInterface {
    private final OrderRepository orderRepository;
    private final ItemRepository productRepository;
    private final CartService cartService;

    public OrderService(OrderRepository orderRepository, ItemRepository productRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.cartService = cartService;
    }

    @Transactional
    @Override
    public Order placeOrder(Long cartId) {
        Cart cart = cartService.getCartByCartId(cartId);
        Order order = createOrder(cart);
        List<OrderItem> orderItemList = createOrderItems(order, cart);
        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalAmount(orderItemList));
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getCartId());
        return savedOrder;
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
    public OrderDto getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }
    @Transactional
    @Override
    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        orders.forEach(order -> Hibernate.initialize(order.getOrderItems()));
        return orders.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private OrderDto convertToDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(order.getOrderId());
        orderDto.setOrderDate(order.getOrderDate().atStartOfDay()); // Convert LocalDate to LocalDateTime
        orderDto.setTotalAmount(order.getTotalAmount());
        orderDto.setStatus(order.getOrderStatus().toString());
        orderDto.setItems(order.getOrderItems().stream().map(this::convertToOrderItemDto).collect(Collectors.toList()));
        return orderDto;
    }

    private OrderItemDto convertToOrderItemDto(OrderItem orderItem) {
        OrderItemDto itemDto = new OrderItemDto();
        itemDto.setItemId(orderItem.getId());
        itemDto.setProductName(orderItem.getItem().getName()); // Assuming Item has a getName() method
        itemDto.setQuantity(orderItem.getQuantity());
        itemDto.setPrice(orderItem.getPrice());
        return itemDto;
    }
}
