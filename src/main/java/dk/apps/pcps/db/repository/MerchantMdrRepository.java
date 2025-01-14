package dk.apps.pcps.db.repository;

import dk.apps.pcps.db.entity.BatchGroup;
import dk.apps.pcps.db.entity.MerchantMdr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface MerchantMdrRepository extends JpaRepository<MerchantMdr, Integer> {

    Optional<MerchantMdr> findByMerchantIdAndBatchGroup(int merchantId, BatchGroup batchGroup);

}
