package dk.apps.pcps.db.service.impl;

import dk.apps.pcps.db.entity.BatchGroup;
import dk.apps.pcps.db.entity.BatchGroupMdr;
import dk.apps.pcps.db.repository.BatchGroupMdrRepository;
import dk.apps.pcps.db.service.BatchGroupMdrService;
import dk.apps.pcps.main.handler.ApplicationException;
import dk.apps.pcps.main.model.enums.ProcessMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;




@Component
public class BatchGroupMdrImpl implements BatchGroupMdrService {

    BatchGroupMdrRepository batchGroupMdrRepository;

    @Autowired
    public BatchGroupMdrImpl(BatchGroupMdrRepository batchGroupMdrRepository){
        this.batchGroupMdrRepository = batchGroupMdrRepository;
    }

    @Override
    public BatchGroupMdr getBatchGroupMdr(BatchGroup batchGroup) {
        return batchGroupMdrRepository.findByBatchGroup(batchGroup)
                .orElseThrow(() -> new ApplicationException(ProcessMessageEnum.BATCH_GROUP_MDR_NOT_CONFIGURED));
    }
}
