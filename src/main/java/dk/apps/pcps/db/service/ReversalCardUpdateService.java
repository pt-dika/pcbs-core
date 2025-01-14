package dk.apps.pcps.db.service;

import dk.apps.pcps.main.model.result.ReversalCardUpdateData;

public interface ReversalCardUpdateService {
    ReversalCardUpdateData getReversal(String username);
    ReversalCardUpdateData createReversal(ReversalCardUpdateData data);
    void removeReversal(String username);

}
