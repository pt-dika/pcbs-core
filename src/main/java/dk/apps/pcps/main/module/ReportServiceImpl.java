package dk.apps.pcps.main.module;

import dk.apps.pcps.commonutils.ISOConverters;
import dk.apps.pcps.commonutils.ObjectMapper;
import dk.apps.pcps.commonutils.Utility;
import dk.apps.pcps.config.PCPSConfig;
import dk.apps.pcps.config.auth.AuthService;
import dk.apps.pcps.db.entity.BatchGroup;
import dk.apps.pcps.db.entity.DeviceUserAndBatchGroup;
import dk.apps.pcps.db.entity.MerchantAndBatchGroup;
import dk.apps.pcps.db.entity.Settlement;
import dk.apps.pcps.db.service.*;
import dk.apps.pcps.main.model.payload.ReqRangeHistory;
import dk.apps.pcps.main.model.payload.TrxHistoryData;
import dk.apps.pcps.main.model.result.*;


import dk.apps.pcps.dbmaster.entity.MobileAppUsers;
import dk.apps.pcps.main.handler.ApplicationException;
import dk.apps.pcps.main.model.enums.ProcessMessageEnum;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    AuthService authService;
    DeviceUserDetailService deviceUserDetailService;
    MerchantAndBatchGroupService merchantAndBatchGroupService;
    DeviceUserAndBatchGroupService deviceUserAndBatchGroupService;
    SuccessPaymentService successPaymentService;
    RefundService refundService;
    SettlementService settlementService;
    TrxHistoryService trxHistoryService;
    SettlementFileUploadService settlementFileUploadService;

    @Autowired
    public ReportServiceImpl(AuthService authService,
                             DeviceUserDetailService deviceUserDetailService,
                             MerchantAndBatchGroupService merchantAndBatchGroupService,
                             DeviceUserAndBatchGroupService deviceUserAndBatchGroupService,
                             SuccessPaymentService successPaymentService,
                                  RefundService refundService,
                                  SettlementService settlementService,
                                  TrxHistoryService trxHistoryService, SettlementFileUploadService settlementFileUploadService) {

        this.authService = authService;
        this.merchantAndBatchGroupService = merchantAndBatchGroupService;
        this.deviceUserDetailService = deviceUserDetailService;
        this.deviceUserAndBatchGroupService = deviceUserAndBatchGroupService;
        this.successPaymentService = successPaymentService;
        this.refundService = refundService;
        this.settlementService = settlementService;
        this.trxHistoryService = trxHistoryService;
        this.settlementFileUploadService = settlementFileUploadService;

    }

    private String getUsername(){
        MobileAppUsers mobileAppUsers = authService.getAuthUsers();
        return mobileAppUsers.getUsername();
    }

    @Override
    public PaymentResult settlementSummary(int batchGroupId) {
        return new PaymentResult().setSummary(settlementFileUploadService.getSettlementSummary(getUsername(), batchGroupId));
    }

    @Override
    public void settlementSessionHistory(BatchGroup batchGroup, ReqRangeHistory req) {

    }

    @Override
    public PaymentResult settlementSummaryHistory(ReqRangeHistory req) {
        MobileAppUsers mobileAppUsers = authService.getAuthUsers();
        String username = mobileAppUsers.getUsername();
        int merchantId = mobileAppUsers.getMerchant().getId();
        String from = req.getFrom();
        String to = req.getTo();
        if (Utility.countDays(from, to) > PCPSConfig.MINUS_DATE)
            throw new ApplicationException(ProcessMessageEnum.OUT_OF_RANGE);
        List<SettlementSessionData> settlementSessionData = new ArrayList<>();
        List<Integer> sessionNumbers = settlementService.getSessionNumbers(username, req.getFrom(), req.getTo());
        for (Integer sessNum : sessionNumbers) {
            List<BatchGroup> batchGroups = settlementService.getBatchGroup(username, sessNum);
            List<BatchGroupData> batchGroupDataList = new ArrayList<>();
            Timestamp settlementAt = null;
            for (BatchGroup batchGroup: batchGroups){
                DeviceUserAndBatchGroup deviceUserAndBatchGroup = deviceUserAndBatchGroupService.getDeviceUserAndBatchGroup(username, batchGroup);
                MerchantAndBatchGroup merchantAndBatchGroup = merchantAndBatchGroupService.getMerchantAndBatchGroup(merchantId, batchGroup);
                String tid = deviceUserAndBatchGroup.getTid();
                String mid = merchantAndBatchGroup.getMid();
                BatchGroupData batchGroupData = ObjectMapper.map(batchGroup, BatchGroupData.class);
                List<Settlement> settlements = settlementService.getDataSettlements(username, sessNum, batchGroup.getId());
                List<SettlementDetail> settlementDetails = new ArrayList<>();
                int trxCount = 0;
                long trxTotalAmount = 0;
                String batchNum = "";

                for (Settlement settlement: settlements){
                    String stan = settlement.getStan() == null?"0":settlement.getStan();
                    int batchNumber = settlement.getBatchNum();
                    int numOfTransaction = settlement.getNumOfSuccessPayment();
                    trxCount += numOfTransaction;
                    long totalBaseAmount = settlement.getSuccessPaymentTotalBaseAmount();
                    trxTotalAmount += totalBaseAmount;
//                    settlementDetails.add(new SettlementDetail()
//                            .setHostDate(settlement.getSettlementHostDate())
//                            .setHostTime(settlement.getSettlementHostTime())
//                            .setBatchNumber(ISOConverters.padLeftZeros(""+batchNumber, 6))
//                            .setStan(ISOConverters.padLeftZeros(""+stan, 6))
//                            .setNumOfTransaction(numOfTransaction)
//                            .setTotalBaseAmount(totalBaseAmount));
                    settlementAt = settlement.getCreateAt();
                    batchNum = ISOConverters.padLeftZeros(""+settlement.getBatchNum(),6);
                }
                batchGroupData.setMid(mid);
                batchGroupData.setTid(tid);
                batchGroupData.setBatchNum(batchNum);
                batchGroupData.setTotalAmount(trxTotalAmount);
                batchGroupData.setTrxCount(trxCount);
//                batchGroupData.setSettlementDetails(settlementDetails);
                batchGroupDataList.add(batchGroupData);
            }
            settlementSessionData.add(new SettlementSessionData().setSessionNumber(sessNum).setSettlementAt(settlementAt).setSettlementSummaryList(batchGroupDataList));
        }
        if (settlementSessionData.size() == 0)
            throw new ApplicationException(ProcessMessageEnum.NOT_FOUND, "summary history list");
        return new PaymentResult().setSummary(settlementSessionData);
    }

    @Override
    public PaymentResult settlementDetailHistory(int sessionNumber) {
        String username = authService.getAuthUsers().getUsername();
        SettlementTrxDetailResult trxDetailResult = new SettlementTrxDetailResult();
        MerchantData merchant = ObjectMapper.map(authService.getAuthUsers().getMerchant(), MerchantData.class);
        trxDetailResult.setMerchant(merchant);
        List<BatchGroup> batchGroups = settlementService.getBatchGroup(username, sessionNumber);
        List<BatchGroupData> batchGroupDataList = new ArrayList<>();
        for (BatchGroup batchGroup: batchGroups){
            List<Settlement> settlements = settlementService.getDataSettlements(username, sessionNumber);
            List<Integer> batchNumbers = settlements
                    .stream()
                    .map(Settlement::getBatchNum)
                    .collect(Collectors.toList());
            List<TrxHistoryData> trxHistoryDataList = trxHistoryService.getTrxHistories(username, batchGroup, batchNumbers);
            long totalBaseAmount = trxHistoryDataList.stream().mapToLong(x -> x.getBaseAmount()).sum();
            BatchGroupData batchGroupData = ObjectMapper.map(batchGroup, BatchGroupData.class);
            batchGroupData.setTotalAmount(totalBaseAmount);
            batchGroupData.setTrxCount(trxHistoryDataList.size());
            batchGroupData.setTrxHistoryList(trxHistoryDataList);
            batchGroupDataList.add(batchGroupData);
        }
        trxDetailResult.setBatchGroupList(batchGroupDataList);
        return new PaymentResult().setDetail(trxDetailResult);
    }

    @Override
    public TrxHistoryResult last10TrxHistory() {
        String username = authService.getAuthUsers().getUsername();
        List<TrxHistoryData> trxHistoryDataList = trxHistoryService.getTrxHistoryLast10(username);
        return new TrxHistoryResult().setTrxHistories(trxHistoryDataList);
    }
}
