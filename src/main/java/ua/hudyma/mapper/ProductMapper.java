package ua.hudyma.mapper;

import ua.hudyma.domain.products.Product;
import ua.hudyma.dto.ProductDto;

public interface ProductMapper {
    ProductDto toDto (Product product);
    Product toEntity (ProductDto productDto);
}
