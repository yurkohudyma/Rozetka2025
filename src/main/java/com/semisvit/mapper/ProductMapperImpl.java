package com.semisvit.mapper;

import com.semisvit.domain.Product;
import com.semisvit.dto.AttribDto;
import com.semisvit.dto.ProductDto;
import org.springframework.stereotype.Component;

@Component
public class ProductMapperImpl implements ProductMapper{
    @Override
    public ProductDto toDto(Product product) {
        var attribList = product
                .getProductPropertiesList()
                .stream()
                .map(pp -> new AttribDto(
                        pp.getAttribute().getAttributeName(),
                        pp.getValue(),
                                  pp.getAttribute().getAttributeType(),
                        pp.getAttributeUnit()))
                .toList();
        return new ProductDto(
                product.getProductName(),
                product.getCategory().getCategoryName(),
                product.getProductCode(),
                attribList
        );
    }

    @Override
    public Product toEntity(ProductDto productDto) {
        return null;
    }
}
