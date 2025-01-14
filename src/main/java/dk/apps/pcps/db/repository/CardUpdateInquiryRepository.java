package dk.apps.pcps.db.repository;

import dk.apps.pcps.db.entity.ReversalCardUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardUpdateInquiryRepository extends JpaRepository<ReversalCardUpdate, Integer> {
    ReversalCardUpdate findByUsername(String username);
}
