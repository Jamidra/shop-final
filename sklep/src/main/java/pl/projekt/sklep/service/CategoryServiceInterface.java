package pl.projekt.sklep.service;

import pl.projekt.sklep.dto.CategoryDto;
import pl.projekt.sklep.exception.AlreadyExistsException;
import pl.projekt.sklep.exception.ResourceNotFoundException;
import pl.projekt.sklep.model.Category;

import java.util.List;

public interface CategoryServiceInterface {
    Category getCategoryById(Long id) throws ResourceNotFoundException;
    Category getCategoryByName(String name) throws ResourceNotFoundException;
    List<Category> getAllCategories();
    CategoryDto addCategory(CategoryDto categoryDto) throws AlreadyExistsException;
    CategoryDto updateCategory(CategoryDto categoryDto, Long id) throws ResourceNotFoundException;
    void deleteCategoryById(Long id) throws ResourceNotFoundException;
}