package dk.apps.pcps.db.repository;

import dk.apps.pcps.db.entity.BatchGroupListTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface BatchGroupListTemplateRepository extends JpaRepository<BatchGroupListTemplate, Integer> {

    List<BatchGroupListTemplate> findAllByNameContains(String name);

}
