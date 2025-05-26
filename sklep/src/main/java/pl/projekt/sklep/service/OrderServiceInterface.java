package pl.projekt.sklep.service;

import java.util.HashMap;

public interface OrderServiceInterface {
    HashMap<String, Object> createOrder(Long cartId);
    String getOrder(Long orderId);
    String getAllOrders();
}