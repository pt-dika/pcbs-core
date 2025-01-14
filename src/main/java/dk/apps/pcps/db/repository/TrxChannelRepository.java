package dk.apps.pcps.db.repository;

import dk.apps.pcps.db.entity.BatchGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dk.apps.pcps.db.entity.TrxChannel;

import java.util.List;

@Repository
public interface TrxChannelRepository extends JpaRepository<TrxChannel, Integer> {
    List<TrxChannel> findAllByBatchGroup(BatchGroup batchGroup);
    List<TrxChannel> findAllByTrxChannelModeAndBatchGroup(String mode, BatchGroup batchGroup);
    Page<TrxChannel> findAllByBatchGroup(BatchGroup batchGroup, Pageable page);
    Page<TrxChannel> findAllByTrxChannelModeAndBatchGroup(String mode, BatchGroup batchGroup, Pageable page);

}
