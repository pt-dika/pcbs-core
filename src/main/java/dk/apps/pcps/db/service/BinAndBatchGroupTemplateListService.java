package dk.apps.pcps.db.service;

import dk.apps.pcps.db.entity.BinAndBatchGroupTemplateList;


import java.util.List;


public interface BinAndBatchGroupTemplateListService {
    BinAndBatchGroupTemplateList getBatchGroupTemplate(int batchGroupTemplateListId, int batchGroupId);
    List<BinAndBatchGroupTemplateList> getTemplateList();
    List<BinAndBatchGroupTemplateList> getTemplateList(int batchGroupTemplateListId);
    List<BinAndBatchGroupTemplateList> getTemplateListChild(int batchGroupTemplateListId);
    BinAndBatchGroupTemplateList getTemplateList(int binId, int batchGroupTemplateListId);
}
