package pl.projekt.sklep.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import pl.projekt.sklep.service.OrderServiceInterface;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order Controller", description = "API for managing orders in the store")
public class OrderController {
    private final OrderServiceInterface orderService;

    public OrderController(OrderServiceInterface orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Create a new order", description = "Places a new order based on a cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order created successfully, returns JSON string with statusCode 200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "500", description = "Internal server error, returns JSON string with statusCode 500",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string")))
    })
    @PostMapping("/neworder")
    public String createOrder(
            @Parameter(description = "ID of the cart which need to be turned into an order", required = true) @RequestParam Long cartId) {
        return orderService.createOrder(cartId).toString();
    }

    @Operation(summary = "Get order by ID", description = "Retrieves a single order by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order retrieved successfully, returns JSON string with statusCode 200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "404", description = "Order not found, returns JSON string with statusCode 404",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string")))
    })
    @GetMapping("/orderId/{orderId}")
    public String getOrderByOrderId(
            @Parameter(description = "ID of the order to retrieve", required = true) @PathVariable Long orderId) {
        return orderService.getOrder(orderId);
    }

    @Operation(summary = "Get all orders", description = "Retrieves all orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully, returns JSON string with statusCode 200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "string")))
    })
    @GetMapping("/all")
    public String getAllOrders() {
        return orderService.getAllOrders();
    }
}