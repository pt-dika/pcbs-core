package dk.apps.pcps.db.service;

import dk.apps.pcps.db.entity.CardUpdateHistory;
import dk.apps.pcps.model.result.CardUpdateHistoryData;

public interface CardUpdateHistoryService {
    CardUpdateHistoryData getCardUpdateHistory(String username);
    CardUpdateHistoryData getCardUpdateHistory(String username, Integer sessionNumber);
    void createCardUpdateHistory(CardUpdateHistory cardUpdateHistory);
    void updateCardUpdateHistory(String username, int sessionNumber);
}
