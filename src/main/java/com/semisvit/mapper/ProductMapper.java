package com.semisvit.mapper;

import com.semisvit.domain.Product;
import com.semisvit.dto.ProductDto;

public interface ProductMapper {
    ProductDto toDto (Product product);
    Product toEntity (ProductDto productDto);
}
