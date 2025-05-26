package pl.projekt.sklep.service;

import org.springframework.dao.DataIntegrityViolationException;
import pl.projekt.sklep.dto.ItemDto;
import pl.projekt.sklep.exception.ResourceNotFoundException;
import pl.projekt.sklep.model.Item;

import java.util.List;

public interface ItemServiceInterface {
    ItemDto addItem(ItemDto itemDto) throws ResourceNotFoundException;
    ItemDto getItemDtoById(Long itemId) throws ResourceNotFoundException;
    String deleteItemById(Long itemId) throws ResourceNotFoundException, DataIntegrityViolationException;
    ItemDto updateItem(ItemDto itemDto, Long itemId) throws ResourceNotFoundException;
    List<ItemDto> getAllItems();
    List<ItemDto> getItemsByCategory(String category) throws ResourceNotFoundException;
    List<ItemDto> getItemsByName(String name) throws ResourceNotFoundException;
    Long countItemsByName(String name);
    Item getItemById(Long itemId);
}