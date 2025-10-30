package ua.hudyma.domain.users;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UpdateTimestamp;
import ua.hudyma.util.IdGenerator;

import java.time.LocalDateTime;

@Table(name = "vendors")
@Entity
@Data
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NaturalId
    private String vendorCode = IdGenerator.generateVendorCode();
    private String vendorName;
    private String vendorAddress;
    @CreationTimestamp
    private LocalDateTime vendorCreatedOn;
    @UpdateTimestamp
    private LocalDateTime vendorUpdatedOn;
}
