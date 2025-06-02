package pl.projekt.sklep.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
    public ItemDto addItem(ItemDto itemDto) throws ResourceNotFoundException {
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
    }

    @Override
    public ItemDto getItemDtoByName(String name) throws ResourceNotFoundException {
        List<Item> items = itemRepository.findByName(name);
        if (items.isEmpty()) {
            throw new ResourceNotFoundException("Item not found with name: " + name);
        }
        return itemMapper.toDto(items.get(0));
    }

    @Override
    public Item getItemByName(String name) {
        List<Item> items = itemRepository.findByName(name);
        if (items.isEmpty()) {
            throw new ResourceNotFoundException("Item not found with name: " + name);
        }
        return items.get(0);
    }

    @Transactional
    @Override
    public String deleteItemByName(String name) throws ResourceNotFoundException {
        List<Item> items = itemRepository.findByName(name);
        if (items.isEmpty()) {
            throw new ResourceNotFoundException("Item not found with name: " + name);
        }
        itemRepository.delete(items.get(0));
        return "Item deleted";
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, String name) throws ResourceNotFoundException {
        Item updatedItem = itemRepository.findByName(name)
                .stream()
                .findFirst()
                .map(existingItem -> updateExistingItem(existingItem, itemDto))
                .map(itemRepository::save)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with name: " + name));
        return itemMapper.toDto(updatedItem);
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
        List<Item> items = itemRepository.findByCategoryName(category);
        if (items.isEmpty()) {
            throw new ResourceNotFoundException("No items found for category: " + category);
        }
        return items.stream().map(itemMapper::toDto).toList();
    }

    @Override
    public List<ItemDto> getItemsByName(String name) throws ResourceNotFoundException {
        List<Item> items = itemRepository.findByName(name);
        if (items.isEmpty()) {
            throw new ResourceNotFoundException("No items found with name: " + name);
        }
        return items.stream().map(itemMapper::toDto).toList();
    }

    @Override
    public Long countItemsByName(String name) {
        return itemRepository.countByName(name);
    }
}