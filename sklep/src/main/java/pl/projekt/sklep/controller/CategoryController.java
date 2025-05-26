package pl.projekt.sklep.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.projekt.sklep.dto.CategoryDto;
import pl.projekt.sklep.dto.ErrorResponseDto;
import pl.projekt.sklep.exception.AlreadyExistsException;
import pl.projekt.sklep.exception.ResourceNotFoundException;
import pl.projekt.sklep.mapper.CategoryMapper;
import pl.projekt.sklep.model.Category;
import pl.projekt.sklep.service.CategoryServiceInterface;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/categories")
@Tag(name = "Category Controller", description = "API for managing categories in the store")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryServiceInterface categoryService;
    private final CategoryMapper categoryMapper;

    @Operation(summary = "Get all categories", description = "Retrieves a list of all categories")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of categories")
    @ApiResponse(responseCode = "404", description = "Categories not found")

    @GetMapping("/all")
    public String getAllCategories() {
        return categoryService.getAllCategories().toString();
    }

    @Operation(summary = "Add a new category", description = "Creates a new category")
    @ApiResponse(responseCode = "200", description = "Category created successfully")
    @ApiResponse(responseCode = "409", description = "Category already exists")

    @PostMapping("/add")
    public String addCategory(
            @Parameter(description = "Category details to add", required = true) @RequestBody CategoryDto categoryDto) throws AlreadyExistsException {
        Category savedCategory = categoryService.addCategory(categoryMapper.toEntity(categoryDto));
        return savedCategory + "added successfully";
    }

    @Operation(summary = "Get category by ID", description = "Retrieves a category by its ID")
    @ApiResponse(responseCode = "200", description = "Category retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Category not found")

    @GetMapping("/id/{id}")
    public String getCategoryById(
            @Parameter(description = "ID of the category to retrieve", required = true) @RequestParam Long id) throws ResourceNotFoundException {
        CategoryDto categoryDto = categoryMapper.toDto(categoryService.getCategoryById(id));
        return categoryDto.toString();
    }

    @Operation(summary = "Get category by name", description = "Retrieves a category by its name")
    @ApiResponse(responseCode = "200", description = "Category retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Category not found")
    @GetMapping("/name/{name}")
    public ResponseEntity<CategoryDto> getCategoryByName(
            @Parameter(description = "Name of the category to retrieve", required = true) @PathVariable String name) throws ResourceNotFoundException {
        Category category = categoryService.getCategoryByName(name);
        CategoryDto categoryDto = categoryMapper.toDto(category);
        return ResponseEntity.ok(categoryDto);
    }

    @Operation(summary = "Delete a category", description = "Deletes a category by its ID")
    @ApiResponse(responseCode = "200", description = "Category deleted successfully")
    @ApiResponse(responseCode = "404", description = "Category not found")
    @DeleteMapping("/delete_by_id")
    public String deleteCategory(
            @Parameter(description = "ID of the category to delete", required = true) @RequestParam Long id) throws ResourceNotFoundException {
        categoryService.deleteCategoryById(id);
        return "Deletion successful";
    }

    @Operation(summary = "Update a category", description = "Updates an existing category by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDto.class))),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PutMapping("/{id}/update")
    public ResponseEntity<CategoryDto> updateCategory(
            @Parameter(description = "ID of the category to update", required = true) @PathVariable Long id,
            @Parameter(description = "Updated category details", required = true) @RequestBody CategoryDto categoryDto) throws ResourceNotFoundException {
        Category category = categoryMapper.toEntity(categoryDto);
        Category updatedCategory = categoryService.updateCategory(category, id);
        CategoryDto updatedCategoryDto = categoryMapper.toDto(updatedCategory);
        return ResponseEntity.ok(updatedCategoryDto);
    }
}