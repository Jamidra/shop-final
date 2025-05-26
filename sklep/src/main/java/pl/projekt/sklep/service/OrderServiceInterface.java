package pl.projekt.sklep.service;

import pl.projekt.sklep.model.Order;

import java.util.HashMap;
import java.util.List;

public interface OrderServiceInterface {
    HashMap<String, Object> createOrder(Long cartId);
    HashMap<String, Object> getOrder(Long orderId);
    List<Order> getAllOrders();
}