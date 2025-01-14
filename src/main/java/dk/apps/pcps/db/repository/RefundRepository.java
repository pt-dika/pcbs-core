package dk.apps.pcps.db.repository;


import dk.apps.pcps.db.entity.BatchGroup;
import dk.apps.pcps.db.entity.Refund;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface RefundRepository extends JpaRepository<Refund, Integer> {
    Optional<Refund> findByInvoiceNumAndUsername(int invoiceNumber, String username);
    Page<Refund> findAllByTidAndMidAndUsernameAndBatchGroup(String tid, String mid, String username, BatchGroup batchGroup, Pageable pageable);
}
