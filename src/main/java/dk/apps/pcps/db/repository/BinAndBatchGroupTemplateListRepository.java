package dk.apps.pcps.db.repository;

import dk.apps.pcps.db.entity.BatchGroupListTemplate;
import dk.apps.pcps.db.entity.BinAndBatchGroupTemplateList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface BinAndBatchGroupTemplateListRepository extends JpaRepository<BinAndBatchGroupTemplateList, Integer> {
    List<BinAndBatchGroupTemplateList> findAllByBatchGroupListTemplate(BatchGroupListTemplate batchGroupTemplateListId);
    List<BinAndBatchGroupTemplateList> findAllByBatchGroupListTemplate_IdAndIsParentFlagTrue(int batchGroupTemplateListId);
    List<BinAndBatchGroupTemplateList> findAllByBatchGroupListTemplate_IdAndIsParentFlagFalse(int batchGroupTemplateListId);
    BinAndBatchGroupTemplateList findByBinIdAndBatchGroupListTemplate_Id(int binId, int batchGroupTemplateListId);
    BinAndBatchGroupTemplateList findByBatchGroup_IdAndBatchGroupListTemplate_Id(int batchGroupId, int batchGroupTemplateListId);
}
