package ua.hudyma.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.hudyma.domain.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.hudyma.domain.AttributeUnit;

import java.util.List;
import java.util.Optional;

public interface AttributeRepository extends JpaRepository<Attribute, Long> {
    boolean existsByAttributeName(String attributeName);

    List<Attribute> findAllByAttributeNameIn(List<String> toList);

    List<Attribute> findAllByCategoryList_CategoryName(String catName);

    @Query(value = """
            SELECT distinct a.*
            FROM categories c
            	join products p on c.id = p.category_id
            		join product_properties pp on pp.product_id = p.id
            			join attributes a on pp.attribute_id = a.id 
            			where category_name = :catName         
                            """,
            nativeQuery = true)
    List<Attribute> findAllCatAttribsByCategoryNameFetched(@Param("catName") String catName);

   /* @Query("""
    SELECT DISTINCT a
    FROM Category c
    JOIN c.Products p
    JOIN p.ProductProperties pp
    JOIN pp.Attribute a
    WHERE c.name = :catName
    """)
    List<Attribute> findAllCatAttribsByCategoryNameFetchedJPQL(@Param("catName") String catName);*/


    Optional<Attribute> findByAttributeName(String attrName);
}
