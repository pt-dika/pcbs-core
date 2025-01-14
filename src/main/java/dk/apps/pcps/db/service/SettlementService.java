package dk.apps.pcps.db.service;

import dk.apps.pcps.db.entity.BatchGroup;
import dk.apps.pcps.db.entity.Settlement;
import dk.apps.pcps.model.result.BatchGroupData;

import java.util.List;

public interface SettlementService {

    List<Integer> getSessionNumbers(String username, String from, String to);
    List<Integer> getSessionNumbers(String username, BatchGroup batchGroup, String from, String to);
    List<BatchGroup> getBatchGroup(String username, Integer sessionNum);
    Settlement getLastSettlement(String username, Integer batchGroupId);
    Settlement getSettlementData(Integer batchGroupId, Integer sessionNum);
    List<Settlement> getDataSettlements(String username, Integer sessionNumber, Integer batchGroupId);
    List<Settlement> getDataSettlements(String username, Integer sessionNumber);
    List<Settlement> getDataSettlements(String username);
    Settlement getDataSettlement(int batchNumber, String username);
    Settlement createDataSettlement(Settlement settlement);
    BatchGroupData settlementSummary(BatchGroup batchGroup);
}
