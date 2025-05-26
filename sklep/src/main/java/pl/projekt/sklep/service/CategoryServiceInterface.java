package pl.projekt.sklep.service;

import pl.projekt.sklep.exception.AlreadyExistsException;
import pl.projekt.sklep.exception.ResourceNotFoundException;
import pl.projekt.sklep.model.Category;

import java.util.List;

public interface CategoryServiceInterface {
    Category getCategoryById(Long id) throws ResourceNotFoundException;
    Category getCategoryByName(String name) throws ResourceNotFoundException;
    List<Category> getAllCategories();
    Category addCategory(Category category) throws AlreadyExistsException;
    Category updateCategory(Category category, Long id) throws ResourceNotFoundException;
    void deleteCategoryById(Long id) throws ResourceNotFoundException;
}