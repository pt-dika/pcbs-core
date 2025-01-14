package dk.apps.pcps.db.repository;

import dk.apps.pcps.db.entity.SettlementFileUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SettlementFileUploadRepository extends JpaRepository<SettlementFileUpload, Integer> {
    @Query(value="SELECT * FROM settlement_file_upload s where s.status in (?1) group by s.batch_group_id", nativeQuery=true)
    List<SettlementFileUpload> findAllByStatusGroupByBatchGroupId(String... status);
    List<SettlementFileUpload> findAllByStatusInOrderByBatchGroupId(String... status);
    @Query(value="SELECT * FROM settlement_file_upload s where s.status in (?1) and is_settle = 1 group by s.username", nativeQuery=true)
    List<SettlementFileUpload> findAllByStatusInGroupByUsername(String... status);
    @Query(value="SELECT * FROM settlement_file_upload s where s.username = ?1 and is_settle = 1 group by s.batch_group_id", nativeQuery=true)
    List<SettlementFileUpload> findAllByUsernameGroupByBatchGroupId(String username);
    List<SettlementFileUpload> findAllByUsernameAndBatchGroupId(String username, int batchGroupId);
    List<SettlementFileUpload> findAllByBatchGroupId(int batchGroupId);
    List<SettlementFileUpload> findAllByBatchGroupIdAndStatus(int batchGroupId, String status);
    List<SettlementFileUpload> findAllByStatus(String status);
    List<SettlementFileUpload> findAllByUsername(String username);
    List<SettlementFileUpload> findAllByUsernameAndAndBatchGroupId(String username, int batchGroupId);
}
