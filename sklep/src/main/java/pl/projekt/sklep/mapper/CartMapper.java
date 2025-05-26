package pl.projekt.sklep.mapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.projekt.sklep.dto.CartDto;
import pl.projekt.sklep.dto.CartItemDto;
import pl.projekt.sklep.dto.ItemDto;
import pl.projekt.sklep.model.Cart;
import pl.projekt.sklep.model.CartItem;
import pl.projekt.sklep.model.Item;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CartMapper {

    private final CategoryMapper categoryMapper;

    @Transactional
    public CartDto toDto(Cart cart) {
        CartDto cartDto = new CartDto();
        cartDto.setCartId(cart.getCartId());
        cartDto.setTotalAmount(cart.getTotalAmount());
        Set<CartItemDto> itemDtos = cart.getItems().stream()
                .map(this::toCartItemDto)
                .collect(Collectors.toSet());
        cartDto.setItems(itemDtos);
        return cartDto;
    }

    private CartItemDto toCartItemDto(CartItem cartItem) {
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setItemId(cartItem.getId());
        cartItemDto.setQuantity(cartItem.getQuantity());
        cartItemDto.setUnitPrice(cartItem.getPrice());
        cartItemDto.setItem(toItemDto(cartItem.getItem()));
        return cartItemDto;
    }

    private ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setItemId(item.getItemId());
        itemDto.setName(item.getName());
        itemDto.setPrice(item.getPrice());
        itemDto.setInventory(item.getInventory());
        itemDto.setDescription(item.getDescription());
        itemDto.setCategory(categoryMapper.toDto(item.getCategory()));
        return itemDto;
    }
}