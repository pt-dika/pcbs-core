package dk.apps.pcps.db.service;

import dk.apps.pcps.db.entity.BatchGroup;
import dk.apps.pcps.db.entity.BatchGroupMdr;


public interface BatchGroupMdrService {
    BatchGroupMdr getBatchGroupMdr(BatchGroup batchGroup);
}
