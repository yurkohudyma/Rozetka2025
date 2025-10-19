package ua.hudyma.service;

import ua.hudyma.domain.*;
import ua.hudyma.dto.AttribDto;
import ua.hudyma.dto.ProductDto;
import ua.hudyma.exception.DtoObligatoryFieldsAreMissingException;
import ua.hudyma.exception.ProductAlreadyExistsException;
import ua.hudyma.mapper.ProductMapper;
import ua.hudyma.repository.AttributeRepository;
import ua.hudyma.repository.CategoryRepository;
import ua.hudyma.repository.ProductRepository;
import ua.hudyma.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Сервіс для управління товарами та їх атрибутами.
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class ProductService {
    private final AttributeRepository attributeRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Transactional(readOnly = true)
    public List<ProductDto> getCatFilteredProducts(String catName, Map<String, List<String>> filterMap) {
        if (filterMap == null || filterMap.isEmpty()) {
            return getAllCategoryProducts(catName);
        }
        /** remove from attribNames concated attribUnit */
        var cleanedFilterMap = filterMap
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey() != null && entry.getValue() != null && !entry.getValue().isEmpty())
                .collect(Collectors.toMap(
                        entry -> entry.getKey().replaceAll("\\s*\\(.*?\\)", ""),
                        entry -> entry.getValue().stream().map(String::trim).toList(),
                        (existing, replacement) -> replacement
                ));
        List<Product> catProductList = productRepository.findAllByCategory_CategoryName(catName);
        List<Product> filteredProducts = new ArrayList<>();

        for (Product product : catProductList) {
            boolean matches = cleanedFilterMap
                    .entrySet()
                    .stream()
                    .allMatch(filterEntry ->
                            product.getProductPropertiesList()
                                    .stream()
                                    .anyMatch(prop -> prop.getAttribute()
                                                    .getAttributeName().equals(filterEntry.getKey()) &&
                                                    filterEntry.getValue().contains(prop.getValue()
                                                    ))
                    );
            if (matches) {
                filteredProducts.add(product);
            }
        }
        return mapListToDto(filteredProducts);
    }

    private List<ProductDto> mapListToDto(List<Product> filteredProducts) {
        return filteredProducts.stream()
                .map(productMapper::toDto)
                .toList();
    }

    //todo exclude price from attribs as it requires different js filtering

    @Transactional(readOnly = true)
    public List<ProductDto> getAllCategoryProducts(String catName) {
        return mapListToDto(productRepository
                .findAllByCategory_CategoryName(catName));
    }

    @Transactional(readOnly = true)
    public Map<String, Set<String>> getAttribMapWithDifferentialSorting(String catName) {
        var allCatProducts = getAllCategoryProducts(catName);
        if (allCatProducts.isEmpty()) {
            return Collections.emptyMap();
        }
        var attrValuesMap = new HashMap<String, List<String>>();
        var attrTypesMap = new HashMap<String, AttributeType>();
        allCatProducts.forEach(productDto -> {
            for (AttribDto attribDto : productDto.attributeList()) {
                var attrName = attribDto.attrName();
                var unitSuffix = attribDto.attribUnit().isEmpty() ? ""
                        : " (" + attribDto.attribUnit() + ")";
                var fullAttrName = attrName + unitSuffix;
                var attrValue = attribDto.attribValue();
                var attrType = attribDto.attributeType();
                attrValuesMap.computeIfAbsent(fullAttrName,
                        k -> new ArrayList<>()).add(attrValue);
                attrTypesMap.putIfAbsent(fullAttrName, attrType);
            }
        });
        var resultMap = new HashMap<String, Set<String>>();
        for (var entry : attrValuesMap.entrySet()) {
            AttributeType type = attrTypesMap
                    .getOrDefault(entry.getKey(), AttributeType.STRING);
            Comparator<String> comparator = switch (type) {
                case NUMBER -> Comparator.comparingDouble(
                        val -> {
                            try {
                                return Double.parseDouble(val);
                            } catch (NumberFormatException e) {
                                return Double.MAX_VALUE;
                            }
                        });
                case BOOLEAN -> Comparator.comparing(Boolean::parseBoolean);
                default -> Comparator.naturalOrder();
            };
            var sortedSet = entry.getValue().stream()
                    .filter(v -> !v.isBlank())
                    .collect(Collectors.toCollection(
                            () -> new TreeSet<>(comparator)));
            resultMap.put(entry.getKey(), sortedSet);
        }
        return resultMap;
    }

    @Transactional(readOnly = true)
    public List<String> getAllCats() {
        return categoryRepository
                .findAll()
                .stream()
                .map(Category::getCategoryName)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductDto> getAllSimple() {
        return mapListToDto(productRepository
                .findAll());
    }


    /**
     * Створює новий товар з атрибутами.
     * <p>
     * Якщо категорія або атрибути ще не існують — вони будуть створені.
     * товар з таким іменем не повинен уже існувати.
     *
     * @param dto DTO з інформацією про товар і атрибути
     * @return DTO з інформацією про створений товар
     * @throws ProductAlreadyExistsException          якщо товар з таким іменем уже існує
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
     * Перевіряє, чи рядкове поле не є null або порожнім.
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
     * Перевіряє, чи товар з таким ім’ям уже існує.
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

    public List<ProductDto> getAllProductDtos() {
        return productRepository
                .findAll()
                .stream()
                .map(product -> new ProductDto(
                        product.getCategory().getCategoryName(),
                        product.getProductName(),
                        product.getProductCode(),
                        product.getProductPrice(),
                        product.getProductPropertiesList()
                                .stream()
                                .filter(pp -> pp.getAttribute() != null)
                                .map(pp -> {
                                    Attribute attribute = pp.getAttribute();
                                    return new AttribDto(
                                            attribute.getAttributeName(),
                                            pp.getValue(),
                                            attribute.getAttributeType(),
                                            pp.getAttributeUnit()
                                    );
                                })
                                .toList()
                ))
                .toList();
    }
}