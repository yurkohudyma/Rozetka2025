package com.semisvit.mapper;

import com.semisvit.domain.Product;
import com.semisvit.dto.ProductReqDto;
import com.semisvit.dto.ProductRespDto;

public interface ProductMapper {
    ProductRespDto toDto (Product product);
    Product toEntity (ProductReqDto productReqDto);
}
