package dk.apps.pcps.db.repository;

import dk.apps.pcps.db.entity.BatchGroup;
import dk.apps.pcps.db.entity.SuccessPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface SuccessPaymentRepository extends JpaRepository<SuccessPayment, Integer> {

    @Query(value="SELECT * FROM success_payment s where s.username = ?1 group by s.batch_group_id", nativeQuery=true)
    List<SuccessPayment> findAllByUsernameGroupByBatchGroupId(String username);
    Page<SuccessPayment> findAllByTidAndMidAndUsernameAndBatchGroup(String tid, String mid, String username, BatchGroup batchGroup, Pageable pageable);
    List<SuccessPayment> findAllByUsername(String username);
    Page<SuccessPayment> findAllByUsername(String username, Pageable page);
    Page<SuccessPayment> findAllByUsernameAndBatchGroup(String username, BatchGroup batchGroup, Pageable page);
    List<SuccessPayment> findAllByUsernameAndBatchGroupOrderByCreateAtDesc(String username, BatchGroup batchGroup);
    List<SuccessPayment> findAllByUsernameAndBatchGroupAndInvoiceNumInOrderByCreateAtDesc(String username, BatchGroup batchGroup, int... invNums);
    List<SuccessPayment> findAllByInvoiceNumIn(List<Integer> invoiceNums);
    SuccessPayment findByInvoiceNumAndUsername(int invoiceNumber, String username);
}
