package dk.apps.pcps.db.repository;

import dk.apps.pcps.db.entity.BatchGroup;
import dk.apps.pcps.db.entity.TrxHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface TrxHistoryRepository extends JpaRepository<TrxHistory, Integer> {
    Optional<TrxHistory> findByUsernameAndBatchGroupAndInvoiceNum(String username, BatchGroup batchGroup, int invoiceNum);
    @Query(value="SELECT * FROM trx_history s where s.username = ?1 group by s.batch_group_id", nativeQuery=true)
    List<TrxHistory> findAllByUsernameGroupByBatchGroupId(String username);
    List<TrxHistory> findAllByUsername(String username);
    List<TrxHistory> findAllByBatchNumAndUsername(int batchNumber, String username);
    List<TrxHistory> findAllByBatchNumInAndUsername(List<Integer> batchNumbers, String username);
    List<TrxHistory> findAllByBatchGroupAndBatchNumInAndUsername(BatchGroup batchGroup, List<Integer> batchNumbers, String username);
    @Query(value="SELECT * FROM trx_history s where s.username = ?1 order by s.create_at desc limit 10", nativeQuery=true)
    List<TrxHistory> findAllByUsernameLast10(String username);
    @Query(value="SELECT * FROM trx_history s where s.username = ?1 and s.batch_group_id = !2 order by s.create_at desc limit 10", nativeQuery=true)
    List<TrxHistory> findAllByUsernameLast10(String username, int batchGroupId);
}
