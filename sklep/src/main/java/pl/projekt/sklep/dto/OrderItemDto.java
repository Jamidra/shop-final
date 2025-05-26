package pl.projekt.sklep.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Data
@Getter
@Setter
public class OrderItemDto {
    private Long itemId;
    private String productName;
    private int quantity;
    private BigDecimal price;

}
