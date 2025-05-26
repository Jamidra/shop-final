package pl.projekt.sklep.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import pl.projekt.sklep.dto.CategoryDto;
import pl.projekt.sklep.dto.ItemDto;
import pl.projekt.sklep.exception.ResourceNotFoundException;
import pl.projekt.sklep.mapper.ItemMapper;
import pl.projekt.sklep.model.Category;
import pl.projekt.sklep.model.Item;
import pl.projekt.sklep.repository.CategoryRepository;
import pl.projekt.sklep.repository.ItemRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService implements ItemServiceInterface {
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final ItemMapper itemMapper;

    @Override
    public Item addItem(Item request) {
        return itemRepository.save(request); // Keep for internal use
    }

    @Override
    public ItemDto addItem(ItemDto itemDto) throws ResourceNotFoundException {
        try {
            Category category = Optional.ofNullable(itemDto.getCategory())
                    .map(CategoryDto::getName)
                    .map(categoryRepository::findByName)
                    .orElseGet(() -> {
                        Category newCategory = new Category();
                        newCategory.setName(itemDto.getCategory().getName());
                        return categoryRepository.save(newCategory);
                    });
            Item item = itemMapper.toEntity(itemDto);
            item.setCategory(category);
            Item savedItem = itemRepository.save(item);
            return itemMapper.toDto(savedItem);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Failed to add item: " + e.getMessage());
        }
    }

    @Override
    public ItemDto getItemDtoById(Long itemId) throws ResourceNotFoundException {
        try {
            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new ResourceNotFoundException("Item not found with ID: " + itemId));
            return itemMapper.toDto(item);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Failed to retrieve item: " + e.getMessage());
        }
    }

    @Override
    public Item getItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(()-> new ResourceNotFoundException("Item not found!"));
    }

    @Transactional
    @Override
    public void deleteItemById(Long itemId) throws ResourceNotFoundException, DataIntegrityViolationException {
        try {
            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new ResourceNotFoundException("Item not found with ID: " + itemId));
            itemRepository.delete(item);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Failed to delete item: " + e.getMessage());
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Cannot delete item with ID " + itemId + " because it is referenced by other records");
        }
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long itemId) throws ResourceNotFoundException {
        try {
            Item updatedItem = itemRepository.findById(itemId)
                    .map(existingItem -> updateExistingItem(existingItem, itemDto))
                    .map(itemRepository::save)
                    .orElseThrow(() -> new ResourceNotFoundException("Item not found with ID: " + itemId));
            return itemMapper.toDto(updatedItem);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Failed to update item: " + e.getMessage());
        }
    }

    private Item updateExistingItem(Item existingItem, ItemDto itemDto) {
        existingItem.setName(itemDto.getName());
        existingItem.setPrice(itemDto.getPrice());
        existingItem.setInventory(itemDto.getInventory());
        existingItem.setDescription(itemDto.getDescription());
        Category category = Optional.ofNullable(itemDto.getCategory())
                .map(CategoryDto::getName)
                .map(categoryRepository::findByName)
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setName(itemDto.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        existingItem.setCategory(category);
        return existingItem;
    }

    @Override
    public List<ItemDto> getAllItems() {
        return itemRepository.findAll()
                .stream()
                .map(itemMapper::toDto)
                .toList();
    }

    @Override
    public List<ItemDto> getItemsByCategory(String category) throws ResourceNotFoundException {
        try {
            List<Item> items = itemRepository.findByCategoryName(category);
            if (items.isEmpty()) {
                throw new ResourceNotFoundException("No items found for category: " + category);
            }
            return items.stream().map(itemMapper::toDto).toList();
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Failed to retrieve items by category: " + e.getMessage());
        }
    }

    @Override
    public List<ItemDto> getItemsByName(String name) throws ResourceNotFoundException {
        try {
            List<Item> items = itemRepository.findByName(name);
            if (items.isEmpty()) {
                throw new ResourceNotFoundException("No items found with name: " + name);
            }
            return items.stream().map(itemMapper::toDto).toList();
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Failed to retrieve items by name: " + e.getMessage());
        }
    }

    @Override
    public Long countItemsByName(String name) {
        return itemRepository.countByName(name);
    }

    @Override
    public List<ItemDto> getConvertedItems(List<Item> items) {
        return items.stream().map(itemMapper::toDto).toList();
    }

    @Override
    public ItemDto convertToDto(Item item) {
        return itemMapper.toDto(item);
    }
}