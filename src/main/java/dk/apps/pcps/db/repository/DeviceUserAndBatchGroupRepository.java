package dk.apps.pcps.db.repository;

import dk.apps.pcps.db.entity.BatchGroup;
import dk.apps.pcps.db.entity.DeviceUserAndBatchGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface DeviceUserAndBatchGroupRepository extends JpaRepository<DeviceUserAndBatchGroup, Integer> {

    @Query(value = "SELECT max(batch_num) FROM device_user_and_batch_group  where username = ?1 and batch_group_id = ?2", nativeQuery = true)
    Integer maxId(String username, int batchGroupId);

    @Query(value = "SELECT max(rrn) FROM device_user_and_batch_group where username = ?1 and batch_group_id = ?2", nativeQuery = true)
    Integer maxRrn(String username, int batchGroupId);

    Optional<DeviceUserAndBatchGroup> findByUsernameAndBatchGroup(String username, BatchGroup batchGroup);

}
