package pl.projekt.sklep.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.projekt.sklep.dto.CategoryDto;
import pl.projekt.sklep.exception.AlreadyExistsException;
import pl.projekt.sklep.exception.ResourceNotFoundException;
import pl.projekt.sklep.mapper.CategoryMapper;
import pl.projekt.sklep.model.Category;
import pl.projekt.sklep.model.Item;
import pl.projekt.sklep.repository.CategoryRepository;
import pl.projekt.sklep.repository.ItemRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements CategoryServiceInterface {
    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;
    private final CategoryMapper categoryMapper;


    @Override
    public Category getCategoryByName(String name) throws ResourceNotFoundException {
        try {
            return categoryRepository.findByName(name);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Failed to retrieve category: " + e.getMessage());
        }
    }

    @Override
    public List<Category> getAllCategories() {
        return (List<Category>) categoryRepository.findAll();
    }

    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) throws AlreadyExistsException {
        try {
            Category category = categoryMapper.toEntity(categoryDto);
            return Optional.of(category)
                    .filter(c -> !categoryRepository.existsByName(c.getName()))
                    .map(categoryRepository::save)
                    .map(categoryMapper::toDto)
                    .orElseThrow(() -> new AlreadyExistsException("Category already exists: " + category.getName()));
        } catch (AlreadyExistsException e) {
            throw new AlreadyExistsException("Failed to add category: " + e.getMessage());
        }
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, String name) throws ResourceNotFoundException {
        try {
            categoryMapper.toEntity(categoryDto);
            return Optional.ofNullable(getCategoryByName(name))
                    .map(oldCategory -> {
                        oldCategory.setName(name);
                        return categoryRepository.save(oldCategory);
                    })
                    .map(categoryMapper::toDto)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with name: " + name));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Failed to update category: " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public void deleteCategoryByName(String name) throws ResourceNotFoundException {
        try {
            Category category = categoryRepository.findByName(name);
            List<Item> items = itemRepository.findByCategoryName(name);
            items.forEach(item -> {
                item.setCategory(null);
                itemRepository.save(item);
            });
            itemRepository.flush();
            categoryRepository.delete(category);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Failed to delete category: " + e.getMessage());
        }
    }
}