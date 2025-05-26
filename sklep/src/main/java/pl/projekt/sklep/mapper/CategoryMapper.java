package pl.projekt.sklep.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.projekt.sklep.dto.CategoryDto;
import pl.projekt.sklep.model.Category;

@Component
@RequiredArgsConstructor
public class CategoryMapper {

    public CategoryDto toDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }

    public Category toEntity(CategoryDto categoryDto) {
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        return category;
    }
}