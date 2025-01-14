package dk.apps.pcps.db.service.impl;

import dk.apps.pcps.db.entity.BatchGroupListTemplate;
import dk.apps.pcps.db.repository.BatchGroupListTemplateRepository;
import dk.apps.pcps.db.service.BatchGroupListTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BatchGroupListTemplateImpl implements BatchGroupListTemplateService {

    BatchGroupListTemplateRepository batchGroupListTemplateRepository;

    @Autowired
    public BatchGroupListTemplateImpl(BatchGroupListTemplateRepository batchGroupListTemplateRepository){
        this.batchGroupListTemplateRepository = batchGroupListTemplateRepository;
    }


    @Override
    public BatchGroupListTemplate getBatchGroupListTemplate(int merchantId) {
        return null;
//        return batchGroupListTemplateRepository.findByBatchGroup(batchGroup)
//                .orElseThrow(() -> new ApplicationException("NOT_FOUND", "batch group", ""+batchGroup.getName(), "not configure batch group mdr,"));
    }
}
