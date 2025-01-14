package dk.apps.pcps.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dk.apps.pcps.db.entity.BatchGroup;


@Repository
public interface BatchGroupRepository extends JpaRepository<BatchGroup, Integer> {

}
