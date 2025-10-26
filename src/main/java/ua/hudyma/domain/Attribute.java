package ua.hudyma.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "attributes")
@Data
public class Attribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    @Column(unique = true)
    private String attributeName;
    String attributeValue;
    @Enumerated(EnumType.STRING)
    private AttributeType attributeType;  // STRING, NUMBER, BOOLEAN, SELECT

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "category_attribute",
            joinColumns = @JoinColumn(name = "attribute_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categoryList = new ArrayList<>();

    @OneToMany(mappedBy = "attribute",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude //invokes SOF otherwise
    @JsonManagedReference
    private Set<AttributeUnit> attributeUnitList = new HashSet<>();
}

