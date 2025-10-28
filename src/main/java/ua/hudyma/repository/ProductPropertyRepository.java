package ua.hudyma.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.hudyma.domain.ProductProperty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductPropertyRepository extends JpaRepository<ProductProperty, Long> {
    boolean existsByAttributeId(Long id);

    Optional<ProductProperty> findByAttributeId(Long id);


    @Query(value = """
            SELECT pp.* FROM product_properties pp
            join attributes a on pp.attribute_id = a.id
            where attribute_name = :attribName""",
            nativeQuery = true)
    List<ProductProperty> findAllValuesByAttribName(@Param("attribName") String attribName);

    @Query(value = """
            SELECT pp.*	FROM categories c
            join products p on c.id = p.category_id
            join product_properties pp on pp.product_id = p.id
            join attributes a on pp.attribute_id = a.id            				               \s
            where category_name = :catName and attribute_name = :attribName""",
            nativeQuery = true)
    List<ProductProperty> findAllValuesByAttribNameAndCatName(
            @Param("attribName") String attribName,
            @Param("catName") String catName);
}
