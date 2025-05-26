package pl.projekt.sklep.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Request object for adding an item to a cart")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddItemToCartRequest {

    @Schema(description = "ID of the cart (optional, creates new cart if not provided)")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @Schema(description = "ID of the item to add", required = true)
    @NotNull(message = "Item ID is required")
    private Long itemId;

    @Schema(description = "Quantity of the item to add", required = true)
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;
}
