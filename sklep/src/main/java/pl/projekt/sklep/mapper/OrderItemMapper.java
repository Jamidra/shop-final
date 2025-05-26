package pl.projekt.sklep.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.projekt.sklep.dto.OrderItemDto;
import pl.projekt.sklep.model.OrderItem;
import pl.projekt.sklep.model.Item;

@Component
@RequiredArgsConstructor
public class OrderItemMapper {

    public OrderItemDto toDto(OrderItem orderItem) {
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setItemId(orderItem.getItem().getItemId());
        orderItemDto.setProductName(orderItem.getItem().getName());
        orderItemDto.setQuantity(orderItem.getQuantity());
        orderItemDto.setPrice(orderItem.getPrice());
        return orderItemDto;
    }

    public OrderItem toEntity(OrderItemDto orderItemDto) {
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(orderItemDto.getQuantity());
        orderItem.setPrice(orderItemDto.getPrice());
        Item item = new Item();
        item.setItemId(orderItemDto.getItemId());
        item.setName(orderItemDto.getProductName());
        orderItem.setItem(item);
        return orderItem;
    }
}