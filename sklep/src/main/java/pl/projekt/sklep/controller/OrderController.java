package pl.projekt.sklep.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.projekt.sklep.dto.OrderDto;
import pl.projekt.sklep.exception.ResourceNotFoundException;

import pl.projekt.sklep.model.Order;
import pl.projekt.sklep.service.OrderServiceInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

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
            @ApiResponse(responseCode = "200", description = "Order created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)))
    })
    @PostMapping("/neworder")
    public ResponseEntity<Map<String, Object>> createOrder(
            @Parameter(description = "ID of the cart which need to be turned into an order", required = true) @RequestParam Long cartId) {
        try {
            Order order = orderService.placeOrder(cartId);
            OrderDto orderdto = orderService.getOrder(order.getOrderId());
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Order created successfully");
            response.put("data", orderdto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Operation(summary = "Get order by ID", description = "Retrieves a single order by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)))
    })
    @GetMapping("/orderId/{orderId}")
    public ResponseEntity<Map<String, Object>> getOrderByOrderId(
            @Parameter(description = "ID of the order to retrieve", required = true) @PathVariable Long orderId) {
        try {
            OrderDto order = orderService.getOrder(orderId);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("data", order);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(NOT_FOUND).body(response);
        }
    }

    @Operation(summary = "Get all orders", description = "Retrieves all orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "No orders found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)))
    })
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllOrders() {
        List<OrderDto> items = orderService.getAllOrders();
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", items);
        return ResponseEntity.ok(response);
    }
}
