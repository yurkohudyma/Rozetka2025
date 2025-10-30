package ua.hudyma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.hudyma.domain.users.Buyer;

public interface BuyerRepository extends JpaRepository<Buyer, Long> {
    boolean existsByBuyerCode(String buyerCode);
}
