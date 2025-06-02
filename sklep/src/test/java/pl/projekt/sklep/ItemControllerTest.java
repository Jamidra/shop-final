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
        ItemDto itemDto = new ItemDto();
        when(itemService.addItem(itemDto)).thenReturn(itemDto);

        ItemDto result = itemController.addItem(itemDto);

        assertEquals(itemDto, result);
        verify(itemService, times(1)).addItem(itemDto);
    }

    @Test
    void addItem_NonExistentCategory_ThrowsResourceNotFoundException() {
        ItemDto itemDto = new ItemDto();
        when(itemService.addItem(itemDto)).thenThrow(new ResourceNotFoundException("Category not found"));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> itemController.addItem(itemDto));
        assertEquals("Category not found", exception.getMessage());
        verify(itemService, times(1)).addItem(itemDto);
    }

    @Test
    void getAllItems_ReturnsItemList() {
        List<ItemDto> items = List.of(new ItemDto());
        when(itemService.getAllItems()).thenReturn(items);

        List<ItemDto> result = itemController.getAllItems();

        assertEquals(items, result);
        verify(itemService, times(1)).getAllItems();
    }

    @Test
    void getItemByName_ValidName_ReturnsItemDto() {
        String name = "Valid";
        ItemDto itemDto = new ItemDto();
        when(itemService.getItemDtoByName(name)).thenReturn(itemDto);

        ItemDto result = itemController.getItemByName(name);

        assertEquals(itemDto, result);
        verify(itemService, times(1)).getItemDtoByName(name);
    }

    @Test
    void updateItem_ValidDtoAndName_ReturnsUpdatedItemDto() {
        String name = "Valid";
        ItemDto itemDto = new ItemDto();
        when(itemService.updateItem(itemDto, name)).thenReturn(itemDto);

        ItemDto result = itemController.updateItem(itemDto, name);

        assertEquals(itemDto, result);
        verify(itemService, times(1)).updateItem(itemDto, name);
    }

    @Test
    void deleteItem_ValidName_ReturnsSuccessMessage() {
        String name = "Valid";
        when(itemService.deleteItemByName(name)).thenReturn("Item deleted");

        String result = itemController.deleteItem(name);

        assertEquals("Item deleted", result);
        verify(itemService, times(1)).deleteItemByName(name);
    }

    @Test
    void getItemsByName_ValidName_ReturnsItemList() {
        String name = "Laptop";
        List<ItemDto> items = List.of(new ItemDto());
        when(itemService.getItemsByName(name)).thenReturn(items);

        List<ItemDto> result = itemController.getItemsByName(name);

        assertEquals(items, result);
        verify(itemService, times(1)).getItemsByName(name);
    }

    @Test
    void findItemByCategory_ValidCategory_ReturnsItemList() {
        String category = "Electronics";
        List<ItemDto> items = List.of(new ItemDto());
        when(itemService.getItemsByCategory(category)).thenReturn(items);

        List<ItemDto> result = itemController.findItemByCategory(category);

        assertEquals(items, result);
        verify(itemService, times(1)).getItemsByCategory(category);
    }

    @Test
    void countItemsByName_ValidName_ReturnsCount() {
        String name = "Laptop";
        Long count = 5L;
        when(itemService.countItemsByName(name)).thenReturn(count);

        Long result = itemController.countItemsByName(name);

        assertEquals(count, result);
        verify(itemService, times(1)).countItemsByName(name);
    }
}