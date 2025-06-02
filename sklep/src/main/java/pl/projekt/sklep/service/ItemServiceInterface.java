package pl.projekt.sklep.service;

import org.springframework.dao.DataIntegrityViolationException;
import pl.projekt.sklep.dto.ItemDto;
import pl.projekt.sklep.exception.ResourceNotFoundException;
import pl.projekt.sklep.model.Item;

import java.util.List;

public interface ItemServiceInterface {
    ItemDto addItem(ItemDto itemDto) throws ResourceNotFoundException;
    ItemDto getItemDtoByName(String name) throws ResourceNotFoundException;
    String deleteItemByName(String name) throws ResourceNotFoundException, DataIntegrityViolationException;
    ItemDto updateItem(ItemDto itemDto, String name) throws ResourceNotFoundException;
    List<ItemDto> getAllItems();
    List<ItemDto> getItemsByCategory(String category) throws ResourceNotFoundException;
    List<ItemDto> getItemsByName(String name) throws ResourceNotFoundException;
    Long countItemsByName(String name);
    Item getItemByName(String name);
}