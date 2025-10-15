package com.semisvit.service;

import com.semisvit.domain.Attribute;
import com.semisvit.domain.Category;
import com.semisvit.domain.Product;
import com.semisvit.domain.ProductProperty;
import com.semisvit.dto.AttribDto;
import com.semisvit.dto.ProductDto;
import com.semisvit.exception.DtoObligatoryFieldsAreMissingException;
import com.semisvit.exception.ProductAlreadyExistsException;
import com.semisvit.mapper.ProductMapper;
import com.semisvit.repository.AttributeRepository;
import com.semisvit.repository.CategoryRepository;
import com.semisvit.repository.ProductRepository;
import com.semisvit.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервіс для управління продуктами та їх атрибутами.
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class ProductService {

    private final AttributeRepository attributeRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<ProductDto> getAllProductDtos() {
        return productRepository
                .findAll()
                .stream()
                .map(product -> new ProductDto(
                        product.getCategory().getCategoryName(),
                        product.getProductName(),
                        product.getProductCode(),
                        product.getProductPropertiesList().stream()
                                .filter(pp -> pp.getAttribute() != null)
                                .map(pp -> {
                                    Attribute attribute = pp.getAttribute();
                                    return new AttribDto(
                                            attribute.getAttributeName(),
                                            pp.getValue(),
                                            attribute.getAttributeType(),
                                            pp.getAttributeUnit() // 🔁 те саме з unit
                                    );
                                })
                                .toList()
                ))
                .toList();
    }


    /**
     * Створює новий продукт з атрибутами.
     * <p>
     * Якщо категорія або атрибути ще не існують — вони будуть створені.
     * Продукт з таким іменем не повинен уже існувати.
     *
     * @param dto DTO з інформацією про продукт і атрибути
     * @return DTO з інформацією про створений продукт
     * @throws ProductAlreadyExistsException          якщо продукт з таким іменем уже існує
     * @throws DtoObligatoryFieldsAreMissingException якщо обов’язкові поля DTO відсутні
     */
    @Transactional
    public ProductDto createProductWithAttributes(ProductDto dto) {
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
        var product = new Product();
        product.setProductName(productName);
        product.setCategory(category);
        product.setProductCode(IdGenerator.generateProductCode(categoryName));
        productRepository.save(product);
        var attributesList = emergeAttributesListFromDtoAndSaveNew(
                dtoAttributesList, category);
        attributesList.forEach(attribute -> {
            var attribReqDto = findValueFromDtoList(dtoAttributesList, attribute);
            var productProperty = createProductProperty(attribute, attribReqDto);
            productProperty.setProduct(product);
            product.getProductPropertiesList().add(productProperty);
        });
        productRepository.save(product);
        product.getProductPropertiesList().forEach(pp ->
                log.info("ProductProperty for {} <-> {}",
                        pp.getProduct().getProductName(),
                        pp.getAttribute().getAttributeName()));
        return productMapper.toDto(product);
    }

    /**
     * Шукає значення атрибута у DTO-списку по назві атрибута.
     *
     * @param dtoAttributesList список атрибутів з DTO
     * @param attribute         атрибут, для якого шукається значення
     * @return знайдений AttribDto
     * @throws DtoObligatoryFieldsAreMissingException якщо атрибут не знайдено
     */
    private AttribDto findValueFromDtoList(List<AttribDto> dtoAttributesList, Attribute attribute) {
        return dtoAttributesList.stream()
                .filter(attr -> attr.attrName().equals(attribute.getAttributeName()))
                .findFirst()
                .orElseThrow(() -> new DtoObligatoryFieldsAreMissingException(
                        "Attribute " + attribute.getAttributeName() + " not found in DTO"));
    }

    /**
     * Створює об'єкт ProductProperty без прив'язки до продукту.
     *
     * @param attribute атрибут
     * @param dto       DTO з даними про значення і одиницю виміру
     * @return створений ProductProperty
     */
    private ProductProperty createProductProperty(Attribute attribute, AttribDto dto) {
        var productProperty = new ProductProperty();
        productProperty.setAttribute(attribute);
        productProperty.setValue(dto.attribValue());
        productProperty.setAttributeUnit(dto.attribUnit());
        return productProperty;
    }

    /**
     * Перевіряє, чи строкове поле не є null або порожнім.
     *
     * @param fieldName значення для перевірки
     * @throws DtoObligatoryFieldsAreMissingException якщо значення некоректне
     */
    private void check(String fieldName) {
        if (fieldName == null || fieldName.trim().isEmpty()) {
            throw new DtoObligatoryFieldsAreMissingException("Obligatory Dto Field is NULL or MISSING");
        }
    }

    /**
     * Перевіряє список атрибутів із DTO, створює нові при потребі, і повертає повний список.
     *
     * @param dtoAttributesList список атрибутів із DTO
     * @param category          категорія, до якої належать атрибути
     * @return список атрибутів, збережених у БД
     */
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
        return attributeRepository
                .findAllByAttributeNameIn(
                        dtoAttributesList
                                .stream()
                                .map(AttribDto::attrName)
                                .toList());
    }

    /**
     * Перевіряє, чи атрибут з таким ім’ям уже існує.
     *
     * @param attributeName назва атрибута
     * @return true — якщо існує, false — інакше
     */
    private boolean attributeExistsByName(String attributeName) {
        return attributeRepository.existsByAttributeName(attributeName);
    }

    /**
     * Перевіряє, чи продукт з таким ім’ям уже існує.
     *
     * @param productName назва продукту
     * @return true — якщо існує, false — інакше
     */
    private boolean productExistsByName(String productName) {
        return productRepository.existsByProductName(productName);
    }

    /**
     * Створює нову категорію з вказаною назвою.
     *
     * @param catName назва категорії
     * @return створена категорія
     */
    private Category createCategory(String catName) {
        var cat = new Category();
        cat.setCategoryName(catName);
        categoryRepository.save(cat);
        log.info("Category '{}' CREATED", catName);
        return cat;
    }
}