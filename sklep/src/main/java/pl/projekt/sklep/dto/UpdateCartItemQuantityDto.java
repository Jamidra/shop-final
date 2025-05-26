package pl.projekt.sklep.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCartItemQuantityDto {

    @NotNull(message = "Cart ID is required")
    @Min(value = 1, message = "Cart ID must be a positive number")
    private Long cartId;

    @NotNull(message = "Item ID is required")
    @Min(value = 1, message = "Item ID must be a positive number")
    private Long itemId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}
