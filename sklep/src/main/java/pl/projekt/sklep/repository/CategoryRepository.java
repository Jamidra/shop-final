package pl.projekt.sklep.repository;

import org.springframework.data.repository.CrudRepository;
import pl.projekt.sklep.model.Category;


public interface CategoryRepository extends CrudRepository<Category, Long> {
    Category findByName(String name);

    boolean existsByName(String name);
}
