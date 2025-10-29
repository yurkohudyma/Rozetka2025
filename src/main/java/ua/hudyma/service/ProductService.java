package ua.hudyma.service;

import jakarta.persistence.EntityNotFoundException;
import ua.hudyma.domain.*;
import ua.hudyma.dto.AttribDto;
import ua.hudyma.dto.MinMaxPricesDto;
import ua.hudyma.dto.ProductDto;
import ua.hudyma.enums.AttributeType;
import ua.hudyma.exception.DtoObligatoryFieldsAreMissingException;
import ua.hudyma.exception.ProductAlreadyExistsException;
import ua.hudyma.mapper.ProductMapper;
import ua.hudyma.repository.AttributeRepository;
import ua.hudyma.repository.AttributeUnitRepository;
import ua.hudyma.repository.CategoryRepository;
import ua.hudyma.repository.ProductRepository;
import ua.hudyma.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    private final AttributeUnitRepository attributeUnitRepository;
    private final ProductMapper productMapper;

    public void deleteAllCats() {
        categoryRepository.deleteAll();
        log.info(":::::::::All cats SUCC deleted");
    }

    public void deleteAllProducts() {
        productRepository.deleteAll();
        log.info("::::::All products SUCC deleted");
    }

    public void deleteProduct(String productCode) {
        var product = productRepository.findByProductCode(productCode);
        product.ifPresent(productRepository::delete);
        log.info("Product {} has been DELETED", productCode);
    }

    @Transactional
    public void editProduct(String productCode, ProductDto dto) {
        var product = productRepository
                .findByProductCode(productCode)
                .orElseThrow(()
                        -> new EntityNotFoundException(":::: Product " +  productCode + " not FOUND"));
        String productName = dto.productName();
        check(productName);
        product.setProductName(productName);
        BigDecimal productPrice = dto.productPrice();
        check(productPrice);
        product.setProductPrice(productPrice);
    }

    //todo implem edit/update product
    public List<ProductDto> filterByPrice (BigDecimal min, BigDecimal max, String catName){
        if (min == null || max == null){
            return mapListToDto(productRepository.findAllByCategory_CategoryName(catName));
        }
        return mapListToDto(productRepository.findAllByCategory_CategoryNameAndProductPriceBetween(catName, min, max));
    }

    @Transactional(readOnly = true)
    public List<AttributeUnit> findAllAttribUnitsByName(String attribName) {
        return attributeUnitRepository
                .findAllByAttribute_AttributeName(attribName);
    }

    @Transactional(readOnly = true)
    public List<ProductDto> getCatFilteredProducts(String catName,
                                                   Map<String, List<String>> filterMap,
                                                   BigDecimal maxPrice, BigDecimal minPrice) {
        if (filterMap == null || filterMap.isEmpty()) {
            return filterByPrice(minPrice, maxPrice, catName);
        }
        var cleanedFilterMap = getCleanedFilterMap(filterMap);
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
            var productPrice = product.getProductPrice();
            boolean priceFitsBetweenRange =  productPrice.compareTo(minPrice) >= 0 &&
                                             productPrice.compareTo(maxPrice) <= 0;
            if (matches && priceFitsBetweenRange) {
                filteredProducts.add(product);
            }
        }
        return mapListToDto(filteredProducts);
    }

    /**
     * removes concated attribUnit from attribNames
     * */
    private static Map<String, List<String>> getCleanedFilterMap(Map<String, List<String>> filterMap) {
        return filterMap
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey() != null && entry.getValue() != null && !entry.getValue().isEmpty())
                .collect(Collectors.toMap(
                        entry -> entry.getKey().replaceAll("\\s*\\(.*?\\)", ""),
                        entry -> entry.getValue().stream().map(String::trim).toList(),
                        (existing, replacement) -> replacement
                ));
    }

    public MinMaxPricesDto getMinMaxPricesDto(List<ProductDto> getCatFilteredProducts) {
        var minPrice = getCatFilteredProducts
                .stream()
                .map(ProductDto::productPrice)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
        var maxPrice = getCatFilteredProducts
                .stream()
                .map(ProductDto::productPrice)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
        return new MinMaxPricesDto(maxPrice, minPrice);
    }

    private List<ProductDto> mapListToDto(List<Product> filteredProducts) {
        return filteredProducts.stream()
                .map(productMapper::toDto)
                .toList();
    }

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
        var category = categoryRepository
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
        product.setProductPrice(dto.productPrice());
        productRepository.save(product);
        var attributesList = extractAttributesListFromDtoAndSaveNew(
                dtoAttributesList, category);
        attributesList.forEach(attribute -> {
            var attribReqDto = findValueFromDtoList(dtoAttributesList, attribute);
            var productProperty = createProductProperty(attribute, attribReqDto);
            productProperty.setProduct(product);
            product.getProductPropertiesList().add(productProperty);
            category.getAttributesList().add(attribute);
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
    private void check(Object fieldName) {
        if (fieldName == null || fieldName.toString().trim().isEmpty()) {
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
    private List<Attribute> extractAttributesListFromDtoAndSaveNew(List<AttribDto> dtoAttributesList, Category category) {
        var newAttributesList = new ArrayList<Attribute>();
        for (AttribDto attr : dtoAttributesList) {
            var attribute = attributeRepository.findByAttributeName(attr.attrName());
            if (attribute.isEmpty()) {
                var newAttribute = new Attribute();
                var uppercasedAttribName = uppercaseFirstLetter (attr.attrName());
                newAttribute.setAttributeName(uppercasedAttribName);
                newAttribute.getCategoryList().add(category);
                newAttribute.setAttributeType(attr.attributeType());
                newAttribute.setAttributeValue(attr.attribValue());
                if (!attr.attribUnit().isEmpty()){
                    var newAttribUnit = createAttribUnit(attr, newAttribute);
                    newAttribute.getAttributeUnitList().add(newAttribUnit);
                }
                newAttributesList.add(newAttribute);
            }
            else {
                var attribOpt = attribute.get();
                category.getAttributesList().add(attribOpt);
                attribOpt.getCategoryList().add(category);
                String attributeValue = attribOpt.getAttributeValue();
                if (attributeValue == null || attributeValue.isEmpty()){
                    attribOpt.setAttributeValue(attr.attribValue());
                }
                if (!attributeUnitRepository.existsByAttribute_IdAndAttribValue(attribOpt.getId(), attr.attribValue())
                        && !attr.attribUnit().isEmpty()){
                    var newAttribUnit = createAttribUnit(attr, attribOpt);
                    attribOpt.getAttributeUnitList().add(newAttribUnit);
                }
            }
        }
        if (!newAttributesList.isEmpty()) {
            attributeRepository.saveAll(newAttributesList);
            log.info("List of {} new attributes CREATED", newAttributesList.size());
        }
        return attributeRepository
                .findAllByAttributeNameIn(
                        dtoAttributesList
                                .stream()
                                .map(AttribDto::attrName)
                                .toList());
    }

    private static AttributeUnit createAttribUnit( AttribDto attr, Attribute newAttribute) {
        var newAttribUnit = new AttributeUnit();
        newAttribUnit.setAttribute(newAttribute);
        newAttribUnit.setAttribValue(attr.attribUnit());
        newAttribute.getAttributeUnitList().add(newAttribUnit);
        return newAttribUnit;
    }

    private String uppercaseFirstLetter(String attrName) {
        if (attrName == null || attrName.isEmpty()) {
            return attrName;
        }
        return attrName.substring(0, 1).toUpperCase() + attrName.substring(1);
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
}