package dk.apps.pcps.db.repository;

import dk.apps.pcps.db.entity.BatchGroup;
import dk.apps.pcps.db.entity.BatchGroupMdr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface BatchGroupMdrRepository extends JpaRepository<BatchGroupMdr, Integer> {

    Optional<BatchGroupMdr> findByBatchGroup(BatchGroup batchGroup);
}
