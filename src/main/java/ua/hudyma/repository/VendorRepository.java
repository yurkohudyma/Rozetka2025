package ua.hudyma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.hudyma.domain.users.Vendor;

public interface VendorRepository extends JpaRepository<Vendor, Long> {
    boolean existsByVendorCode(String vendorCode);
}
