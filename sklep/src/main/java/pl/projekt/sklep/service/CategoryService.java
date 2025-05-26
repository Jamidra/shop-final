package pl.projekt.sklep.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.projekt.sklep.exception.AlreadyExistsException;
import pl.projekt.sklep.exception.ResourceNotFoundException;
import pl.projekt.sklep.model.Category;
import pl.projekt.sklep.repository.CategoryRepository;
import pl.projekt.sklep.repository.ItemRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements CategoryServiceInterface {
    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;

    @Override
    public Category getCategoryById(Long id) throws ResourceNotFoundException {
        try {
            return categoryRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Failed to retrieve category: " + e.getMessage());
        }
    }

    @Override
    public Category getCategoryByName(String name) throws ResourceNotFoundException {
        try {
            return Optional.ofNullable(categoryRepository.findByName(name))
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with name: " + name));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Failed to retrieve category: " + e.getMessage());
        }
    }

    @Override
    public List<Category> getAllCategories() {
        return (List<Category>) categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) throws AlreadyExistsException {
        try {
            return Optional.of(category)
                    .filter(c -> !categoryRepository.existsByName(c.getName()))
                    .map(categoryRepository::save)
                    .orElseThrow(() -> new AlreadyExistsException("Category already exists: " + category.getName()));
        } catch (AlreadyExistsException e) {
            throw new AlreadyExistsException("Failed to add category: " + e.getMessage());
        }
    }

    @Override
    public Category updateCategory(Category category, Long id) throws ResourceNotFoundException {
        try {
            return Optional.ofNullable(getCategoryById(id))
                    .map(oldCategory -> {
                        oldCategory.setName(category.getName());
                        return categoryRepository.save(oldCategory);
                    })
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Failed to update category: " + e.getMessage());
        }
    }

    @Override
    public void deleteCategoryById(Long id) throws ResourceNotFoundException {
        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));

            category.getItems().forEach(item -> {
                item.setCategory(null);
                itemRepository.save(item);
            });
            // Now delete the category
            categoryRepository.delete(category);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Failed to delete category: " + e.getMessage());
        }
    }
}