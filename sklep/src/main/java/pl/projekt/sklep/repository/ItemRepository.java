package pl.projekt.sklep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.projekt.sklep.model.Item;

import java.util.List;
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByCategoryName(String category);
    List<Item> findByName(String name);
    List<Item> findByCategoryId(Long categoryId);
    Long countByName(String name);
}
