package pl.projekt.sklep.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemDto {
    private Long itemId;
    private String name;
    private BigDecimal price;
    private int inventory;
    private String description;
    private CategoryDto category;
}