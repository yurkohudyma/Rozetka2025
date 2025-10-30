package ua.hudyma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.hudyma.domain.users.Vendor;

import java.util.Optional;

public interface VendorRepository extends JpaRepository<Vendor, Long> {
    boolean existsByVendorCode(String vendorCode);

    Optional<Vendor> findByVendorCode(String vendorCode);
}
