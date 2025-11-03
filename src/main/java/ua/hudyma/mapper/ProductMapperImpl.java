package ua.hudyma.mapper;

import org.springframework.stereotype.Component;
import ua.hudyma.domain.products.Product;
import ua.hudyma.dto.AttribDto;
import ua.hudyma.dto.ProductDto;

import static java.util.Comparator.comparing;

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
                .sorted(comparing(AttribDto::attrName))
                .toList();
        return new ProductDto(
                product.getProductName(),
                product.getProductName(),
                product.getProductCode(),
                product.getProductPrice(),
                product.getVendorCode(),
                product.getImageUrl(),
                attribList
        );
    }

    @Override
    public Product toEntity(ProductDto productDto) {
        throw  new IllegalStateException("Method NOT implemented");
    }
}
