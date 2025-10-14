package com.semisvit.mapper;

import com.semisvit.domain.Product;
import com.semisvit.dto.AttribDto;
import com.semisvit.dto.ProductReqDto;
import com.semisvit.dto.ProductRespDto;
import org.springframework.stereotype.Component;

@Component
public class ProductMapperImpl implements ProductMapper{
    @Override
    public ProductRespDto toDto(Product product) {
        var attribList = product
                .getProductPropertiesList()
                .stream()
                .map(pp -> new AttribDto(
                        pp.getAttribute().getAttributeName(),
                        pp.getValue(),
                                  pp.getAttribute().getAttributeType(),
                        pp.getAttributeUnit()))
                .toList();
        return new ProductRespDto(
                product.getProductName(),
                product.getCategory().getCategoryName(),
                attribList
        );
    }

    @Override
    public Product toEntity(ProductReqDto productReqDto) {
        return null;
    }
}
