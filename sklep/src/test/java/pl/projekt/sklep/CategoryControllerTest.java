package pl.projekt.sklep;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.projekt.sklep.controller.CategoryController;
import pl.projekt.sklep.dto.CategoryDto;
import pl.projekt.sklep.exception.AlreadyExistsException;
import pl.projekt.sklep.exception.ResourceNotFoundException;
import pl.projekt.sklep.model.Category;
import pl.projekt.sklep.service.CategoryServiceInterface;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryControllerTest {

    @Mock
    private CategoryServiceInterface categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCategories_ReturnsCategoriesList() {
        List<Category> categories = List.of(new Category());
        when(categoryService.getAllCategories()).thenReturn(categories);

        String result = categoryController.getAllCategories();

        assertEquals(categories.toString(), result);
        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    void addCategory_ValidDto_ReturnsCategoryDtoString() {
        CategoryDto categoryDto = new CategoryDto();
        when(categoryService.addCategory(categoryDto)).thenReturn(categoryDto);

        String result = categoryController.addCategory(categoryDto);

        assertEquals(categoryDto.toString(), result);
        verify(categoryService, times(1)).addCategory(categoryDto);
    }

    @Test
    void addCategory_AlreadyExists_ThrowsAlreadyExistsException() {
        CategoryDto categoryDto = new CategoryDto();
        when(categoryService.addCategory(categoryDto)).thenThrow(new AlreadyExistsException("Category already exists"));

        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class, () -> categoryController.addCategory(categoryDto));
        assertEquals("Category already exists", exception.getMessage());
        verify(categoryService, times(1)).addCategory(categoryDto);
    }

    @Test
    void getCategoryByName_ValidName_ReturnsCategoryString() {
        String name = "Electronics";
        Category category = new Category();
        when(categoryService.getCategoryByName(name)).thenReturn(category);

        String result = categoryController.getCategoryByName(name);

        assertEquals(category.toString(), result);
        verify(categoryService, times(1)).getCategoryByName(name);
    }

    @Test
    void getCategoryByName_NonExistentName_ThrowsResourceNotFoundException() {
        String name = "NonExistent";
        when(categoryService.getCategoryByName(name)).thenThrow(new ResourceNotFoundException("Category not found with name: NonExistent"));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> categoryController.getCategoryByName(name));
        assertEquals("Category not found with name: NonExistent", exception.getMessage());
        verify(categoryService, times(1)).getCategoryByName(name);
    }

    @Test
    void deleteCategory_ValidId_ReturnsSuccessMessage() {
        String name = "Valid";
        doNothing().when(categoryService).deleteCategoryByName(name);

        String result = categoryController.deleteCategory(name);

        assertEquals("Deletion successful", result);
        verify(categoryService, times(1)).deleteCategoryByName(name);
    }

    @Test
    void deleteCategory_NonExistentId_ThrowsResourceNotFoundException() {
        String name = "Invalid";
        doThrow(new ResourceNotFoundException("Category not found with name: Invalid")).when(categoryService).deleteCategoryByName(name);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> categoryController.deleteCategory(name));
        assertEquals("Category not found with name: Invalid", exception.getMessage());
        verify(categoryService, times(1)).deleteCategoryByName(name);
    }

    @Test
    void updateCategory_ValidDtoAndName_ReturnsUpdatedCategoryDtoString() {
        String name = "Valid";
        CategoryDto categoryDto = new CategoryDto();
        when(categoryService.updateCategory(categoryDto, name)).thenReturn(categoryDto);

        CategoryDto result = categoryController.updateCategory(categoryDto, name);

        assertEquals(categoryDto, result);
        verify(categoryService, times(1)).updateCategory(categoryDto, name);
    }

    @Test
    void updateCategory_NonExistentName_ThrowsResourceNotFoundException() {
        String name = "Invalid";
        CategoryDto categoryDto = new CategoryDto();
        when(categoryService.updateCategory(categoryDto, name)).thenThrow(new ResourceNotFoundException("Category not found with ID: 999"));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> categoryController.updateCategory(categoryDto, name));
        assertEquals("Category not found with ID: 999", exception.getMessage());
        verify(categoryService, times(1)).updateCategory(categoryDto, name);
    }
}
