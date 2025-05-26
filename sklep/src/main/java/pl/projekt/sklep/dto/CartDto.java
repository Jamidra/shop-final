package pl.projekt.sklep.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Setter
@Data
@Getter
public class CartDto {
    private Long cartId;
    private Set<CartItemDto> items;
    private BigDecimal totalAmount;

}
