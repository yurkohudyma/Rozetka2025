package ua.hudyma.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "attrib_units")
@Data
public class AttributeUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "attrib_id")
    @JsonBackReference
    private Attribute attribute;
    private String value;
}
