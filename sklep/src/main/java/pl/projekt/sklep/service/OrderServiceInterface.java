package pl.projekt.sklep.service;

import pl.projekt.sklep.dto.OrderDto;
import pl.projekt.sklep.model.Order;

import java.util.List;

public interface OrderServiceInterface {
    Order placeOrder(Long cartId);
    OrderDto getOrder(Long orderId);

    List<OrderDto> getAllOrders();
}
