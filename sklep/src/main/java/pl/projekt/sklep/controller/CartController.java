package pl.projekt.sklep.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import pl.projekt.sklep.dto.CartDto;
import pl.projekt.sklep.service.CartServiceInterface;

import java.math.BigDecimal;

@RestController
@RequestMapping("api/cart")
@Tag(name = "Cart Controller", description = "API for managing shopping carts")
public class CartController {
    private final CartServiceInterface cartService;

    public CartController(CartServiceInterface cartService) {
        this.cartService = cartService;
    }

    @Operation(summary = "Get cart by ID", description = "Retrieves a cart by its ID")
    @ApiResponse(responseCode = "200", description = "Cart retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Cart not found")

    @GetMapping("/get_cart_by_id")
    public CartDto getCart(
            @Parameter(description = "ID of the cart to retrieve", required = true) @RequestParam Long cartId) {
        return cartService.getCartDto(cartId);
    }

    @Operation(summary = "Clear a cart", description = "Removes all items from a cart")
    @ApiResponse(responseCode = "200", description = "Cart cleared successfully")
    @ApiResponse(responseCode = "404", description = "Cart not found")

    @DeleteMapping("/clear_cart_by_id")
    public String clearCart(
            @Parameter(description = "ID of the cart to clear", required = true) @RequestParam Long cartId) {
        cartService.clearCart(cartId);
        return "Cart cleared successfully";
    }

    @Operation(summary = "Get total price of cart", description = "Calculates the total price of items in a cart")
    @ApiResponse(responseCode = "200", description = "Total price retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Cart not found")

    @GetMapping("/cartid/total-price")
    public BigDecimal getTotalAmount(
            @Parameter(description = "ID of the cart to calculate total price", required = true) @RequestParam Long cartId) {
        return cartService.getTotalPrice(cartId);
    }
}