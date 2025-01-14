package dk.apps.pcps.db.repository;

import dk.apps.pcps.db.entity.MerchantDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface MerchantDetailRepository extends JpaRepository<MerchantDetail, Integer> {
    Optional<MerchantDetail> findByMerchantId(int merchantId);
}
