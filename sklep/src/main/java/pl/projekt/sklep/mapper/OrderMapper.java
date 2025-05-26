package pl.projekt.sklep.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.projekt.sklep.dto.OrderDto;
import pl.projekt.sklep.dto.OrderItemDto;
import pl.projekt.sklep.model.Order;
import pl.projekt.sklep.model.OrderItem;
import pl.projekt.sklep.model.OrderStatus;

import java.util.HashSet;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final OrderItemMapper orderItemMapper;

    public OrderDto toDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(order.getOrderId());
        orderDto.setOrderDate(order.getOrderDate());
        orderDto.setTotalAmount(order.getTotalAmount());
        orderDto.setStatus(order.getOrderStatus().name());
        orderDto.setItems(order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toList()));
        return orderDto;
    }

    public Order toEntity(OrderDto orderDto) {
        Order order = new Order();
        order.setOrderId(orderDto.getOrderId());
        order.setOrderDate(orderDto.getOrderDate());
        order.setTotalAmount(orderDto.getTotalAmount());
        order.setOrderStatus(OrderStatus.valueOf(orderDto.getStatus()));
        order.setOrderItems(new HashSet<>(orderDto.getItems().stream()
                .map(orderItemMapper::toEntity)
                .peek(orderItem -> orderItem.setOrder(order))
                .collect(Collectors.toList())));
        return order;
    }
}