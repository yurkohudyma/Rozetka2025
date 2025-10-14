package com.semisvit.service;

import com.semisvit.domain.Attribute;
import com.semisvit.domain.Category;
import com.semisvit.domain.Product;
import com.semisvit.domain.ProductProperty;
import com.semisvit.dto.AttribDto;
import com.semisvit.dto.ProductReqDto;
import com.semisvit.dto.ProductRespDto;
import com.semisvit.exception.DtoObligatoryFieldsAreMissingException;
import com.semisvit.exception.ProductAlreadyExistsException;
import com.semisvit.mapper.ProductMapper;
import com.semisvit.repository.AttributeRepository;
import com.semisvit.repository.CategoryRepository;
import com.semisvit.repository.ProductPropertyRepository;
import com.semisvit.repository.ProductRepository;
import com.semisvit.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductService {
    private final AttributeRepository attributeRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Transactional
    public ProductRespDto createProductWithAttributes(ProductReqDto dto) {
        var productName = dto.productName();
        check(productName);

        if (productExistsByName(productName)) {
            throw new ProductAlreadyExistsException("Product already exists with supplied name: " + productName);
        }

        var categoryName = dto.categoryName();
        check(categoryName);

        Category category = categoryRepository
                .findByCategoryName(categoryName)
                .orElseGet(() -> createCategory(categoryName));

        var dtoAttributesList = dto.attributeList();
        if (dtoAttributesList == null || dtoAttributesList.isEmpty()) {
            throw new DtoObligatoryFieldsAreMissingException("DtoAttribList is EMPTY or null");
        }

        // Створення продукту
        var product = new Product();
        product.setProductName(productName);
        product.setCategory(category);
        product.setProductCode(IdGenerator.generateProductCode(categoryName));

        // Спочатку зберігаємо продукт, щоб мати ID
        productRepository.save(product);

        // Отримуємо або створюємо атрибути
        var attributesList = emergeAttributesListFromDtoAndSaveNew(dtoAttributesList, category);

        // Створюємо зв’язки ProductProperty і додаємо їх до продукту
        attributesList.forEach(attribute -> {
            var attribReqDto = findValueFromDtoList(dtoAttributesList, attribute);
            var productProperty = createProductProperty(attribute, attribReqDto);

            // Встановлюємо зв'язок в обидва боки
            productProperty.setProduct(product);
            product.getProductPropertiesList().add(productProperty);
        });

        // Зберігаємо продукт з каскадним збереженням властивостей
        productRepository.save(product); // можливо вдруге, але це не проблема

        // Логування
        product.getProductPropertiesList().forEach(pp ->
                log.info("ProductProperty for {} <-> {}",
                        pp.getProduct().getProductName(),
                        pp.getAttribute().getAttributeName()));

        return productMapper.toDto(product);
    }

    private AttribDto findValueFromDtoList(List<AttribDto> dtoAttributesList, Attribute attribute) {
        return dtoAttributesList.stream()
                .filter(attr -> attr.attrName().equals(attribute.getAttributeName()))
                .findFirst()
                .orElseThrow(() -> new DtoObligatoryFieldsAreMissingException(
                        "Attribute " + attribute.getAttributeName() + " not found in DTO"));
    }

    private ProductProperty createProductProperty(Attribute attribute, AttribDto dto) {
        var productProperty = new ProductProperty();
        productProperty.setAttribute(attribute);
        productProperty.setValue(dto.attribValue());
        productProperty.setAttributeUnit(dto.attribUnit());
        return productProperty;
    }

    private void check(String fieldName) {
        if (fieldName == null || fieldName.trim().isEmpty()) {
            throw new DtoObligatoryFieldsAreMissingException("Obligatory Dto Field is NULL or MISSING");
        }
    }

    @Transactional
    private List<Attribute> emergeAttributesListFromDtoAndSaveNew(List<AttribDto> dtoAttributesList, Category category) {
        var newAttributes = new ArrayList<Attribute>();

        for (AttribDto attr : dtoAttributesList) {
            if (!attributeExistsByName(attr.attrName())) {
                var attribute = new Attribute();
                attribute.setAttributeName(attr.attrName());
                attribute.setCategory(category);
                attribute.setAttributeType(attr.attributeType());
                newAttributes.add(attribute);
            }
        }

        if (!newAttributes.isEmpty()) {
            attributeRepository.saveAll(newAttributes);
            log.info("List of {} new attributes CREATED", newAttributes.size());
        }

        // Повторно отримуємо всі атрибути по іменах

        return attributeRepository
                .findAllByAttributeNameIn(
                dtoAttributesList
                        .stream()
                        .map(AttribDto::attrName)
                        .toList());
    }

    private boolean attributeExistsByName(String attributeName) {
        return attributeRepository.existsByAttributeName(attributeName);
    }

    private boolean productExistsByName(String productName) {
        return productRepository.existsByProductName(productName);
    }

    private Category createCategory(String catName) {
        var cat = new Category();
        cat.setCategoryName(catName);
        categoryRepository.save(cat);
        log.info("Category '{}' CREATED", catName);
        return cat;
    }
}
