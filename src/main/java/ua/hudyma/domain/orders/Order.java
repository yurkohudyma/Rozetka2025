package ua.hudyma.domain.orders;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NaturalId
    private String orderCode = UUID.randomUUID().toString();
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private String buyerId; //todo implement entity
    private String vendorId; //todo implement entity
    private BigDecimal orderTotal;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private List<OrderProductDto> productDTOList = new ArrayList<>();
    @CreationTimestamp
    private LocalDateTime orderCreatedOn;
    @UpdateTimestamp
    private LocalDateTime orderUpdatedOn;



}
