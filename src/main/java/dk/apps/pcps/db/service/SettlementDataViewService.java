package dk.apps.pcps.db.service;

import dk.apps.pcps.db.entity.BatchGroup;
import dk.apps.pcps.db.entity.SettlementDataView;

import java.util.List;
import java.util.Map;

public interface SettlementDataViewService {
    List<BatchGroup> getBatchGroup(String username);
    Map countSettlementData(String username, int limit);
    Map countSettlementData(BatchGroup batchGroup, String username, int limit);
    String getRandomUser();
    SettlementDataView getRandomData();
    SettlementDataView getRandomData(String username);
    List<SettlementDataView> getDataForSettlement(SettlementDataView req, int page, int limit, String sortBy);
    List<SettlementDataView> getDataForSettlement(String username, int page, int limit, String sortBy);
    List<SettlementDataView> getDataForSettlement(BatchGroup batchGroup, String username);
    List<SettlementDataView> getDataForSettlement(BatchGroup batchGroup, String username, int page, int limit, String sortBy);
}
