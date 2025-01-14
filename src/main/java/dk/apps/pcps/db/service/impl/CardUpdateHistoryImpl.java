package dk.apps.pcps.db.service.impl;

import dk.apps.pcps.commonutils.Utility;
import dk.apps.pcps.db.entity.CardUpdateHistory;
import dk.apps.pcps.db.repository.CardUpdateHistoryRepository;
import dk.apps.pcps.db.service.CardUpdateHistoryService;
import dk.apps.pcps.model.iso8583.bni.model.TransactionLogResult;
import dk.apps.pcps.model.result.CardUpdateHistoryData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CardUpdateHistoryImpl implements CardUpdateHistoryService {

    CardUpdateHistoryRepository cardUpdateHistoryRepository;

    @Autowired
    public CardUpdateHistoryImpl(CardUpdateHistoryRepository cardUpdateHistoryRepository) {
        this.cardUpdateHistoryRepository = cardUpdateHistoryRepository;
    }

    @Override
    public CardUpdateHistoryData getCardUpdateHistory(String username) {
        return getCardUpdateHistory(username, null);
    }

    @Override
    public CardUpdateHistoryData getCardUpdateHistory(String username, Integer sessionNumber) {
        int count = 0;
        long totalAmount = 0;
        List<CardUpdateHistory> histories;
        if (sessionNumber == null)
            histories = cardUpdateHistoryRepository.findAllByUsernameAndSessionNumberIsNull(username);
        else
            histories = cardUpdateHistoryRepository.findAllByUsernameAndSessionNumber(username, sessionNumber);

        for (CardUpdateHistory history:histories){
            TransactionLogResult trxLog = Utility.jsonToObject(history.getAdditionalData(), TransactionLogResult.class);
            totalAmount += Long.parseLong(trxLog.getAmount(), 16);
            count++;
        }
        return new CardUpdateHistoryData().setCount(count).setTotalAmount(totalAmount);
    }

    @Override
    public void createCardUpdateHistory(CardUpdateHistory cardUpdateHistory) {
        cardUpdateHistoryRepository.save(cardUpdateHistory);
    }

    @Override
    public void updateCardUpdateHistory(String username, int sessionNumber) {
        List<CardUpdateHistory> histories = cardUpdateHistoryRepository.findAllByUsernameAndSessionNumberIsNull(username);
        histories.forEach(
                f -> {
                    f.setSessionNumber(sessionNumber);
                }
        );
        if (histories.size() > 0)
            cardUpdateHistoryRepository.saveAll(histories);
    }
}
