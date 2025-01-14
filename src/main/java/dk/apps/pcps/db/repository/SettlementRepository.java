package dk.apps.pcps.db.repository;

import dk.apps.pcps.db.entity.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface SettlementRepository extends JpaRepository<Settlement, Integer> {
    Settlement findFirstByUsernameAndBatchGroupIdOrderByCreateAtDesc(String username, Integer batchGroupId);
    Settlement findFirstByUsernameAndBatchGroupIdAndSettlementSessionNum(String username, Integer batchGroupId, Integer sessionNumber);
    @Query(value="SELECT * FROM settlement s where s.username = ?1 and create_at between ?2 and ?3 group by s.settlement_session_num order by s.create_at desc", nativeQuery=true)
    List<Settlement> findAllByUsernameGroupBySessNum(String username, String from, String to);
    @Query(value="SELECT * FROM settlement s where s.username = ?1 group by s.settlement_session_num order by s.create_at desc limit 10", nativeQuery=true)
    List<Settlement> findAllByUsernameGroupBySessNum(String username);
    @Query(value="SELECT * FROM settlement s where s.username = ?1 and s.settlement_session_num = ?2 group by s.batch_group_id order by s.create_at desc", nativeQuery=true)
    List<Settlement> findAllByUsernameAndSessionNumGroupByBatchGroupId(String username, Integer sessNum);
    List<Settlement> findAllByUsernameAndSettlementSessionNumAndBatchGroupId(String username, Integer sessionNumber, Integer batchGroupId);
    List<Settlement> findAllByUsernameAndSettlementSessionNum(String username, Integer sessionNumber);
    List<Settlement> findAllByUsername(String username);
    //List<Settlement> findAllByUsernameAndCreateAtBetween(String username, Date from, Date to);
    Optional<Settlement> findByBatchNumAndUsername(int batchNumber, String username);

}
