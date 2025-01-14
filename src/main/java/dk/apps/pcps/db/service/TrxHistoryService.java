package dk.apps.pcps.db.service;

import dk.apps.pcps.db.entity.BatchGroup;
import dk.apps.pcps.db.entity.TrxHistory;

import dk.apps.pcps.main.model.payload.TrxHistoryData;

import java.util.List;

public interface TrxHistoryService {
    List<BatchGroup> getBatchGroup(String username);
    List<TrxHistory> getTrxHistories(String username);
    List<TrxHistory> getTrxHistories(String username, List<Integer> batchNumbers);
    TrxHistoryData getTrxHistory(String username, BatchGroup batchGroup, int invoiceNum);
    List<TrxHistoryData> getTrxHistories(String username, BatchGroup batchGroup, List<Integer> batchNumbers);
    List<TrxHistory> getTrxHistory(int batchNumber, String username);
    List<TrxHistoryData> getTrxHistoryLast10(String username);
    List<TrxHistory> getTrxHistoryLast10(String username, int batchGroupId);
    List<TrxHistory> createTrxHistory(List<TrxHistory> trxHistorys);
}
