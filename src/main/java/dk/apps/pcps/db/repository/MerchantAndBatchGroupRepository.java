package dk.apps.pcps.db.repository;

import dk.apps.pcps.db.entity.BatchGroup;
import dk.apps.pcps.db.entity.MerchantAndBatchGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface MerchantAndBatchGroupRepository extends JpaRepository<MerchantAndBatchGroup, Integer> {

    Optional<MerchantAndBatchGroup> findByMerchantIdAndBatchGroup(int merchantId, BatchGroup batchGroup);
}
