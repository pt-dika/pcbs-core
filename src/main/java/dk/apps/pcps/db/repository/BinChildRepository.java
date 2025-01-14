package dk.apps.pcps.db.repository;

import dk.apps.pcps.db.entity.BinChild;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BinChildRepository extends JpaRepository<BinChild, Integer> {

}
