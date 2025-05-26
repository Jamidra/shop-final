package pl.projekt.sklep.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import pl.projekt.sklep.dto.OrderDto;
import pl.projekt.sklep.mapper.OrderMapper;
import pl.projekt.sklep.service.OrderServiceInterface;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order Controller", description = "API for managing orders in the store")
public class OrderController {
    private final OrderServiceInterface orderService;
    private final OrderMapper orderMapper;

    public OrderController(OrderServiceInterface orderService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @Operation(summary = "Create a new order", description = "Places a new order based on a cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HashMap.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HashMap.class)))
    })
    @PostMapping("/neworder")
    public HashMap<String, Object> createOrder(
            @Parameter(description = "ID of the cart which needs to be turned into an order", required = true)
            @RequestParam Long cartId) {
        return orderService.createOrder(cartId);
    }

    @Operation(summary = "Get order by ID", description = "Retrieves a single order by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HashMap.class))),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HashMap.class)))
    })
    @GetMapping("/orderId")
    public HashMap<String, Object> getOrderByOrderId(
            @Parameter(description = "ID of the order to retrieve", required = true)
            @RequestParam Long orderId) {
        return orderService.getOrder(orderId);
    }

    @Operation(summary = "Get all orders", description = "Retrieves all orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class)))
    })
    @GetMapping("/all")
    public List<OrderDto> getAllOrders() {
        return orderService.getAllOrders().stream()
                .map(orderMapper::toDto)
                .toList();
    }
}