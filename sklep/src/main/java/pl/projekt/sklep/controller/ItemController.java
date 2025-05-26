package pl.projekt.sklep.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;
import pl.projekt.sklep.dto.ErrorResponseDto;
import pl.projekt.sklep.dto.ItemDto;
import pl.projekt.sklep.exception.ResourceNotFoundException;
import pl.projekt.sklep.service.ItemServiceInterface;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Item Controller", description = "API for managing items in the store")
@RequiredArgsConstructor
public class ItemController {
    private final ItemServiceInterface itemService;

    @Operation(summary = "Add a new item", description = "Creates a new item in the store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item created successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PostMapping("/add")
    public ItemDto addItem(
            @Parameter(description = "Item details to add", required = true) @RequestBody ItemDto itemDto) throws ResourceNotFoundException {
        return itemService.addItem(itemDto);
    }

    @Operation(summary = "Get all items", description = "Retrieves a list of all items in the store")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of items")

    @GetMapping("/all")
    public List<ItemDto> getAllItems() {
        return itemService.getAllItems();
    }

    @Operation(summary = "Get item by ID", description = "Retrieves a single item by its ID")

            @ApiResponse(responseCode = "200", description = "Successfully retrieved item")
            @ApiResponse(responseCode = "404", description = "Item not found")

    @GetMapping("/by_id")
    public ItemDto getItemById(
            @Parameter(description = "ID of the item to retrieve", required = true) @RequestParam Long itemId) throws ResourceNotFoundException {
        return itemService.getItemDtoById(itemId);
    }

    @Operation(summary = "Update an item", description = "Updates an existing item by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ItemDto.class))),
            @ApiResponse(responseCode = "404", description = "Item not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PutMapping("/{itemId}/update")
    public ItemDto updateItem(
            @Parameter(description = "Updated item details", required = true) @RequestBody ItemDto itemDto,
            @Parameter(description = "ID of the item to update", required = true) @PathVariable Long itemId) throws ResourceNotFoundException {
        return itemService.updateItem(itemDto, itemId);
    }

    @Operation(summary = "Delete an item", description = "Deletes an item by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item deleted successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Item not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Item cannot be deleted due to references",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @DeleteMapping("/delete")
    public String deleteItem(
            @Parameter(description = "ID of the item to delete", required = true) @RequestParam Long itemId) throws ResourceNotFoundException, DataIntegrityViolationException {
        return itemService.deleteItemById(itemId);
    }

    @Operation(summary = "Get items by name", description = "Retrieves a list of items by their name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved items",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ItemDto.class))),
            @ApiResponse(responseCode = "404", description = "No items found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/by_name")
    public String getItemByName(
            @Parameter(description = "Name of the item to search for", required = true) @RequestParam String name) throws ResourceNotFoundException {
        return itemService.getItemsByName(name).toString();
    }

    @Operation(summary = "Get items by category", description = "Retrieves a list of items by their category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved items",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ItemDto.class))),
            @ApiResponse(responseCode = "404", description = "No items found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/{category}/all/items")
    public List<ItemDto> findItemByCategory(
            @Parameter(description = "Category of the items to retrieve", required = true) @PathVariable String category) throws ResourceNotFoundException {
        return itemService.getItemsByCategory(category);
    }

    @Operation(summary = "Count items by name", description = "Counts the number of items with the specified name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully counted items",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/item/count/by-name")
    public Long countItemsByName(
            @Parameter(description = "Name of the items to count", required = true) @RequestParam String name) {
        return itemService.countItemsByName(name);
    }
}