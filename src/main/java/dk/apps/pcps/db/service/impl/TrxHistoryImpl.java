package dk.apps.pcps.db.service.impl;

import dk.apps.pcps.commonutils.ISOConverters;
import dk.apps.pcps.commonutils.ObjectMapper;
import dk.apps.pcps.db.entity.BatchGroup;
import dk.apps.pcps.db.entity.TrxHistory;
import dk.apps.pcps.db.repository.TrxHistoryRepository;
import dk.apps.pcps.db.service.TrxHistoryService;
import dk.apps.pcps.main.handler.ApplicationException;
import dk.apps.pcps.main.model.enums.ProcessMessageEnum;
import dk.apps.pcps.main.model.payload.TrxHistoryData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;




import java.util.List;
import java.util.stream.Collectors;

@Component
public class TrxHistoryImpl implements TrxHistoryService {

    TrxHistoryRepository trxHistoryRepository;

    @Autowired
    public TrxHistoryImpl(TrxHistoryRepository trxHistoryRepository){
        this.trxHistoryRepository = trxHistoryRepository;
    }


    @Override
    public List<BatchGroup> getBatchGroup(String username) {
        List<TrxHistory> trxHistories = trxHistoryRepository.findAllByUsernameGroupByBatchGroupId(username);
        List<BatchGroup> batchGroups = trxHistories.stream().map(TrxHistory::getBatchGroup).collect(Collectors.toList());
        if (batchGroups.size() == 0)
            throw new ApplicationException(ProcessMessageEnum.NOT_FOUND, "trx history");
        return batchGroups;
    }

    @Override
    public List<TrxHistory> getTrxHistories(String username) {
        return trxHistoryRepository.findAllByUsername(username);
    }

    @Override
    public List<TrxHistory> getTrxHistories(String username, List<Integer> batchNumbers) {
        return trxHistoryRepository.findAllByBatchNumInAndUsername(batchNumbers, username);
    }

    @Override
    public TrxHistoryData getTrxHistory(String username, BatchGroup batchGroup, int invoiceNum) {
        TrxHistory trxHistory = trxHistoryRepository.findByUsernameAndBatchGroupAndInvoiceNum(username, batchGroup, invoiceNum)
                .orElseThrow(() -> new ApplicationException(ProcessMessageEnum.NOT_FOUND));
        TrxHistoryData trxHistoryData = ObjectMapper.map(trxHistory, TrxHistoryData.class);
        trxHistoryData.setInvoiceNum(ISOConverters.padLeftZeros(""+trxHistory.getInvoiceNum(), 6));
        return trxHistoryData;
    }

    @Override
    public List<TrxHistoryData> getTrxHistories(String username, BatchGroup batchGroup, List<Integer> batchNumbers) {
        List<TrxHistory> trxHistories = trxHistoryRepository.findAllByBatchGroupAndBatchNumInAndUsername(batchGroup, batchNumbers, username);
        List<TrxHistoryData> trxHistoryDataList = ObjectMapper.mapAll(trxHistories, TrxHistoryData.class);
        trxHistoryDataList = invoiceNumToString(trxHistoryDataList);
        return trxHistoryDataList;
    }

    @Override
    public List<TrxHistory> getTrxHistory(int batchNumber, String username) {
        return trxHistoryRepository.findAllByBatchNumAndUsername(batchNumber, username);
    }

    @Override
    public List<TrxHistoryData> getTrxHistoryLast10(String username) {
        List<TrxHistory> trxHistories = trxHistoryRepository.findAllByUsernameLast10(username);
        if (trxHistories.size() == 0)
            throw new ApplicationException(ProcessMessageEnum.NOT_FOUND, "last 10 trx history");
        List<TrxHistoryData> trxHistoryDataList = ObjectMapper.mapAll(trxHistories, TrxHistoryData.class);
        trxHistoryDataList = invoiceNumToString(trxHistoryDataList);
        return trxHistoryDataList;
    }

    @Override
    public List<TrxHistory> getTrxHistoryLast10(String username, int batchGroupId) {
        return trxHistoryRepository.findAllByUsernameLast10(username, batchGroupId);
    }

    @Override
    public List<TrxHistory> createTrxHistory(List<TrxHistory> trxHistorys) {
        return trxHistoryRepository.saveAll(trxHistorys);
    }

    private List<TrxHistoryData> invoiceNumToString(List<TrxHistoryData> trxHistoryData){
        trxHistoryData.forEach(
                f -> {
                    f.setInvoiceNum(ISOConverters.padLeftZeros(""+f.getInvoiceNum(), 6));
                }
        );
        return trxHistoryData;
    }
}
