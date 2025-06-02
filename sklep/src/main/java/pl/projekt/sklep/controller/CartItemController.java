package pl.projekt.sklep.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.projekt.sklep.dto.AddItemToCartRequest;
import pl.projekt.sklep.dto.UpdateCartItemQuantityDto;
import pl.projekt.sklep.exception.ResourceNotFoundException;
import pl.projekt.sklep.service.CartItemServiceInterface;

@RestController
@RequestMapping("api/cartItems")
@Tag(name = "Cart Item Controller", description = "API for managing items in a cart")
@RequiredArgsConstructor
public class CartItemController {
    private final CartItemServiceInterface cartItemService;

    @Operation(summary = "Add an item to a cart", description = "Adds a specified quantity of an item to a cart. Creates a new cart if cartId is not provided.")
    @ApiResponse(responseCode = "200", description = "Item successfully added to cart")
    @ApiResponse(responseCode = "404", description = "Cart or item not found")
    @PostMapping("/item/add")
    public String addItemToCart(@RequestBody AddItemToCartRequest request) {
        cartItemService.addItemAndInitialize(request.getCartId(), request.getItemName(), request.getQuantity());
        return "Added successfully";
    }

    @Operation(summary = "Remove item from cart", description = "Removes an item from a cart")
    @ApiResponse(responseCode = "200", description = "Item removed from cart successfully")
    @ApiResponse(responseCode = "404", description = "Cart or item not found")
    @DeleteMapping("/cart/remove_by_name")
    public String removeItemFromCart(
            @Parameter(description = "ID of the cart", required = true) @RequestParam Long cartId,
            @Parameter(description = "Name of the item to remove", required = true) @RequestParam String itemName) throws ResourceNotFoundException {
        cartItemService.removeItemFromCart(cartId, itemName);
        return "Item removed successfully";
    }

    @Operation(summary = "Update item quantity in cart", description = "Updates the quantity of an item in a cart")
    @ApiResponse(responseCode = "200", description = "Item quantity updated successfully")
    @ApiResponse(responseCode = "404", description = "Cart or item not found")
    @PutMapping("/item/update")
    public String updateItemQuantity(
            @Parameter(description = "Details of the cart item to update", required = true)
            @Valid @RequestBody UpdateCartItemQuantityDto request) throws ResourceNotFoundException {
        cartItemService.updateItemQuantity(request.getCartId(), request.getItemName(), request.getQuantity());
        return "Updated successfully";
    }
}