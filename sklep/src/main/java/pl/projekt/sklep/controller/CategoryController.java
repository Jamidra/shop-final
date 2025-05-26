package pl.projekt.sklep.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.projekt.sklep.dto.CategoryDto;
import pl.projekt.sklep.exception.AlreadyExistsException;
import pl.projekt.sklep.exception.ResourceNotFoundException;
import pl.projekt.sklep.mapper.CategoryMapper;
import pl.projekt.sklep.service.CategoryServiceInterface;

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
        return categoryService.addCategory(categoryDto).toString();
    }

    @Operation(summary = "Get category by ID", description = "Retrieves a category by its ID")
    @ApiResponse(responseCode = "200", description = "Category retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Category not found")
    @GetMapping("/id")
    public String getCategoryById(
            @Parameter(description = "ID of the category to retrieve", required = true) @RequestParam Long id) throws ResourceNotFoundException {
        return categoryMapper.toDto(categoryService.getCategoryById(id)).toString();
    }

    @Operation(summary = "Get category by name", description = "Retrieves a category by its name")
    @ApiResponse(responseCode = "200", description = "Category retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Category not found")
    @GetMapping("/name/")
    public String getCategoryByName(
            @Parameter(description = "Name of the category to retrieve", required = true) @RequestParam String name) throws ResourceNotFoundException {
        return categoryService.getCategoryByName(name).toString();
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
    @ApiResponse(responseCode = "200", description = "Category updated successfully")
    @ApiResponse(responseCode = "404", description = "Category not found")
    @PutMapping("/update_category/{id}")
    public String updateCategory(
            @Parameter(description = "Updated category details", required = true) @RequestBody CategoryDto categoryDto,
            @Parameter(description = "ID of the category to update", required = true) @PathVariable Long id) throws ResourceNotFoundException {
        return categoryService.updateCategory(categoryDto, id).toString();
    }
}