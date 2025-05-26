package pl.projekt.sklep;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.projekt.sklep.controller.ItemController;
import pl.projekt.sklep.dto.ItemDto;
import pl.projekt.sklep.exception.ResourceNotFoundException;
import pl.projekt.sklep.service.ItemServiceInterface;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemControllerTest {

    @Mock
    private ItemServiceInterface itemService;

    @InjectMocks
    private ItemController itemController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addItem_ValidDto_ReturnsItemDto() {
        // Arrange
        ItemDto itemDto = new ItemDto();
        when(itemService.addItem(itemDto)).thenReturn(itemDto);

        // Act
        ItemDto result = itemController.addItem(itemDto);

        // Assert
        assertEquals(itemDto, result);
        verify(itemService, times(1)).addItem(itemDto);
    }

    @Test
    void addItem_NonExistentCategory_ThrowsResourceNotFoundException() {
        // Arrange
        ItemDto itemDto = new ItemDto();
        when(itemService.addItem(itemDto)).thenThrow(new ResourceNotFoundException("Category not found"));

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> itemController.addItem(itemDto));
        assertEquals("Category not found", exception.getMessage());
        verify(itemService, times(1)).addItem(itemDto);
    }

    @Test
    void getAllItems_ReturnsItemList() {
        // Arrange
        List<ItemDto> items = List.of(new ItemDto());
        when(itemService.getAllItems()).thenReturn(items);

        // Act
        List<ItemDto> result = itemController.getAllItems();

        // Assert
        assertEquals(items, result);
        verify(itemService, times(1)).getAllItems();
    }

    @Test
    void getItemById_ValidId_ReturnsItemDto() {
        // Arrange
        Long itemId = 1L;
        ItemDto itemDto = new ItemDto();
        when(itemService.getItemDtoById(itemId)).thenReturn(itemDto);

        // Act
        ItemDto result = itemController.getItemById(itemId);

        // Assert
        assertEquals(itemDto, result);
        verify(itemService, times(1)).getItemDtoById(itemId);
    }

    @Test
    void updateItem_ValidDtoAndId_ReturnsUpdatedItemDto() {
        // Arrange
        Long itemId = 1L;
        ItemDto itemDto = new ItemDto();
        when(itemService.updateItem(itemDto, itemId)).thenReturn(itemDto);

        // Act
        ItemDto result = itemController.updateItem(itemDto, itemId);

        // Assert
        assertEquals(itemDto, result);
        verify(itemService, times(1)).updateItem(itemDto, itemId);
    }

    @Test
    void deleteItem_ValidId_ReturnsSuccessMessage() {
        // Arrange
        Long itemId = 1L;
        when(itemService.deleteItemById(itemId)).thenReturn("Item deleted");

        // Act
        String result = itemController.deleteItem(itemId);

        // Assert
        assertEquals("Item deleted", result);
        verify(itemService, times(1)).deleteItemById(itemId);
    }

    @Test
    void getItemByName_ValidName_ReturnsItemsString() {
        // Arrange
        String name = "Laptop";
        List<ItemDto> items = List.of(new ItemDto());
        when(itemService.getItemsByName(name)).thenReturn(items);

        // Act
        String result = itemController.getItemByName(name);

        // Assert
        assertEquals(items.toString(), result);
        verify(itemService, times(1)).getItemsByName(name);
    }

    @Test
    void findItemByCategory_ValidCategory_ReturnsItemList() {
        // Arrange
        String category = "Electronics";
        List<ItemDto> items = List.of(new ItemDto());
        when(itemService.getItemsByCategory(category)).thenReturn(items);

        // Act
        List<ItemDto> result = itemController.findItemByCategory(category);

        // Assert
        assertEquals(items, result);
        verify(itemService, times(1)).getItemsByCategory(category);
    }

    @Test
    void countItemsByName_ValidName_ReturnsCount() {
        // Arrange
        String name = "Laptop";
        Long count = 5L;
        when(itemService.countItemsByName(name)).thenReturn(count);

        // Act
        Long result = itemController.countItemsByName(name);

        // Assert
        assertEquals(count, result);
        verify(itemService, times(1)).countItemsByName(name);
    }
}