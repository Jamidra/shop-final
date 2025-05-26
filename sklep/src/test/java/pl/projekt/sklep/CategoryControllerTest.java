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
import pl.projekt.sklep.mapper.CategoryMapper;
import pl.projekt.sklep.model.Category;
import pl.projekt.sklep.service.CategoryServiceInterface;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryControllerTest {

    @Mock
    private CategoryServiceInterface categoryService;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCategories_ReturnsCategoriesList() {
        // Arrange
        List<Category> categories = List.of(new Category());
        when(categoryService.getAllCategories()).thenReturn(categories);

        // Act
        String result = categoryController.getAllCategories();

        // Assert
        assertEquals(categories.toString(), result);
        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    void addCategory_ValidDto_ReturnsCategoryDtoString() {
        // Arrange
        CategoryDto categoryDto = new CategoryDto();
        when(categoryService.addCategory(categoryDto)).thenReturn(categoryDto);

        // Act
        String result = categoryController.addCategory(categoryDto);

        // Assert
        assertEquals(categoryDto.toString(), result);
        verify(categoryService, times(1)).addCategory(categoryDto);
    }

    @Test
    void addCategory_AlreadyExists_ThrowsAlreadyExistsException() {
        // Arrange
        CategoryDto categoryDto = new CategoryDto();
        when(categoryService.addCategory(categoryDto)).thenThrow(new AlreadyExistsException("Category already exists"));

        // Act & Assert
        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class, () -> categoryController.addCategory(categoryDto));
        assertEquals("Category already exists", exception.getMessage());
        verify(categoryService, times(1)).addCategory(categoryDto);
    }

    @Test
    void getCategoryById_ValidId_ReturnsCategoryDtoString() {
        // Arrange
        Long id = 1L;
        Category category = new Category();
        CategoryDto categoryDto = new CategoryDto();
        when(categoryService.getCategoryById(id)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        // Act
        String result = categoryController.getCategoryById(id);

        // Assert
        assertEquals(categoryDto.toString(), result);
        verify(categoryService, times(1)).getCategoryById(id);
        verify(categoryMapper, times(1)).toDto(category);
    }

    @Test
    void getCategoryById_NonExistentId_ThrowsResourceNotFoundException() {
        // Arrange
        Long id = 999L;
        when(categoryService.getCategoryById(id)).thenThrow(new ResourceNotFoundException("Category not found with ID: 999"));

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> categoryController.getCategoryById(id));
        assertEquals("Category not found with ID: 999", exception.getMessage());
        verify(categoryService, times(1)).getCategoryById(id);
    }

    @Test
    void getCategoryByName_ValidName_ReturnsCategoryString() {
        // Arrange
        String name = "Electronics";
        Category category = new Category();
        when(categoryService.getCategoryByName(name)).thenReturn(category);

        // Act
        String result = categoryController.getCategoryByName(name);

        // Assert
        assertEquals(category.toString(), result);
        verify(categoryService, times(1)).getCategoryByName(name);
    }

    @Test
    void getCategoryByName_NonExistentName_ThrowsResourceNotFoundException() {
        // Arrange
        String name = "NonExistent";
        when(categoryService.getCategoryByName(name)).thenThrow(new ResourceNotFoundException("Category not found with name: NonExistent"));

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> categoryController.getCategoryByName(name));
        assertEquals("Category not found with name: NonExistent", exception.getMessage());
        verify(categoryService, times(1)).getCategoryByName(name);
    }

    @Test
    void deleteCategory_ValidId_ReturnsSuccessMessage() {
        // Arrange
        Long id = 1L;
        doNothing().when(categoryService).deleteCategoryById(id);

        // Act
        String result = categoryController.deleteCategory(id);

        // Assert
        assertEquals("Deletion successful", result);
        verify(categoryService, times(1)).deleteCategoryById(id);
    }

    @Test
    void deleteCategory_NonExistentId_ThrowsResourceNotFoundException() {
        // Arrange
        Long id = 999L;
        doThrow(new ResourceNotFoundException("Category not found with ID: 999")).when(categoryService).deleteCategoryById(id);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> categoryController.deleteCategory(id));
        assertEquals("Category not found with ID: 999", exception.getMessage());
        verify(categoryService, times(1)).deleteCategoryById(id);
    }

    @Test
    void updateCategory_ValidDtoAndId_ReturnsUpdatedCategoryDtoString() {
        // Arrange
        Long id = 1L;
        CategoryDto categoryDto = new CategoryDto();
        when(categoryService.updateCategory(categoryDto, id)).thenReturn(categoryDto);

        // Act
        String result = categoryController.updateCategory(categoryDto, id);

        // Assert
        assertEquals(categoryDto.toString(), result);
        verify(categoryService, times(1)).updateCategory(categoryDto, id);
    }

    @Test
    void updateCategory_NonExistentId_ThrowsResourceNotFoundException() {
        // Arrange
        Long id = 999L;
        CategoryDto categoryDto = new CategoryDto();
        when(categoryService.updateCategory(categoryDto, id)).thenThrow(new ResourceNotFoundException("Category not found with ID: 999"));

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> categoryController.updateCategory(categoryDto, id));
        assertEquals("Category not found with ID: 999", exception.getMessage());
        verify(categoryService, times(1)).updateCategory(categoryDto, id);
    }
}
