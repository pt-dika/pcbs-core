package dk.apps.pcps.db.service.impl;

import dk.apps.pcps.main.handler.ApplicationException;
import dk.apps.pcps.main.model.enums.ProcessMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import dk.apps.pcps.db.entity.BatchGroup;
import dk.apps.pcps.db.repository.BatchGroupRepository;
import dk.apps.pcps.db.service.BatchGroupService;

@Component
public class BatchGroupImpl implements BatchGroupService {

    BatchGroupRepository batchGroupRepository;

    @Autowired
    public BatchGroupImpl(BatchGroupRepository batchGroupRepository){
        this.batchGroupRepository = batchGroupRepository;
    }

    @Override
    public BatchGroup getBatchGroup(int batchGroupId) {
        return batchGroupRepository.findById(batchGroupId)
                .orElseThrow(() -> new ApplicationException(ProcessMessageEnum.NOT_FOUND));
    }
}
