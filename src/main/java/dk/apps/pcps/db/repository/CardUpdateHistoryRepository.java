package dk.apps.pcps.db.repository;

import dk.apps.pcps.db.entity.CardUpdateHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardUpdateHistoryRepository extends JpaRepository<CardUpdateHistory, Integer> {
    List<CardUpdateHistory> findAllByUsernameAndSessionNumberIsNull(String username);
    List<CardUpdateHistory> findAllByUsernameAndSessionNumber(String username, int sessionNumber);
}
