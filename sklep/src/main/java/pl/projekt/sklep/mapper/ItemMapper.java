package pl.projekt.sklep.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.projekt.sklep.dto.CategoryDto;
import pl.projekt.sklep.dto.ItemDto;
import pl.projekt.sklep.model.Category;
import pl.projekt.sklep.model.Item;

@Component
@RequiredArgsConstructor
public class ItemMapper {

    public ItemDto toDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setItemId(item.getItemId());
        itemDto.setName(item.getName());
        itemDto.setPrice(item.getPrice());
        itemDto.setInventory(item.getInventory());
        itemDto.setDescription(item.getDescription());
        itemDto.setCategory(toCategoryDto(item.getCategory()));
        return itemDto;
    }

    public Item toEntity(ItemDto itemDto) {
        Item item = new Item();
        item.setItemId(itemDto.getItemId());
        item.setName(itemDto.getName());
        item.setPrice(itemDto.getPrice());
        item.setInventory(itemDto.getInventory());
        item.setDescription(itemDto.getDescription());
        item.setCategory(toCategoryEntity(itemDto.getCategory()));
        return item;
    }

    private CategoryDto toCategoryDto(Category category) {
        if (category == null) return null;
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }

    private Category toCategoryEntity(CategoryDto categoryDto) {
        if (categoryDto == null) return null;
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        return category;
    }
}