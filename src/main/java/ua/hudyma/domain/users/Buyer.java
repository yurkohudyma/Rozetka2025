package ua.hudyma.domain.users;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UpdateTimestamp;
import ua.hudyma.util.IdGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "buyers")
@Entity
@Data
public class Buyer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NaturalId
    private String buyerCode = IdGenerator.generateBuyerCode();
    private String fullName;
    private String buyerAddress;
    private BigDecimal account = BigDecimal.ZERO;
    @CreationTimestamp
    private LocalDateTime buyerCreatedOn;
    @UpdateTimestamp
    private LocalDateTime buyerUpdatedOn;
}
