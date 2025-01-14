package dk.apps.pcps.db.repository;

import dk.apps.pcps.db.entity.BatchGroup;
import dk.apps.pcps.db.entity.SettlementDataView;
import dk.apps.pcps.db.entity.TrxChannel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface SettlementDataViewRepository extends JpaRepository<SettlementDataView, Integer> {

    List<SettlementDataView> findAllByRefund(Boolean isRefund);
    Page<SettlementDataView> findAllByRefund(Boolean isRefund, Pageable pageable);
    List<SettlementDataView> findAllByUsernameAndRefund(String username, Boolean isRefund);
    Page<SettlementDataView> findAllByUsernameAndRefund(String username, Boolean isRefund, Pageable pageable);
    List<SettlementDataView> findAllByBatchGroupAndUsernameAndRefund(BatchGroup batchGroup, String username, Boolean isRefund);
    Page<SettlementDataView> findAllByBatchGroupAndUsernameAndRefund(BatchGroup batchGroup, String username, Boolean isRefund, Pageable pageable);
    @Query(value="SELECT * FROM settlement_data_view s where s.username = ?1 group by s.batch_group_id", nativeQuery=true)
    List<SettlementDataView> findAllByUsernameGroupByBatchGroupId(String username);
    Page<SettlementDataView> findAllByTrxChannelAndUsernameAndRefund(TrxChannel trxChannel, String username, Boolean isRefund, Pageable pageable);
    Page<SettlementDataView> findAllByTidAndMidAndUsernameAndBatchGroupAndRefund(String tid, String mid, String username, BatchGroup batchGroup, Boolean refund, Pageable pageable);
}
