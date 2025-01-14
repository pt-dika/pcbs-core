package dk.apps.pcps.db.repository;

import dk.apps.pcps.db.entity.BinParent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BinParentRepository extends JpaRepository<BinParent, Integer> {

}
