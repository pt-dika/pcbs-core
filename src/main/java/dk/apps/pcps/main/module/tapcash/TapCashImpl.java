package dk.apps.pcps.main.module.tapcash;


import dk.apps.pcps.commonutils.TripleDESFunction;
import dk.apps.pcps.config.PCPSConfig;
import dk.apps.pcps.config.auth.AuthService;
import dk.apps.pcps.db.entity.*;
import dk.apps.pcps.db.service.*;
import dk.apps.pcps.main.handler.ApplicationException;
import dk.apps.pcps.main.handler.MessageException;
import dk.apps.pcps.main.model.enums.CardEnum;
import dk.apps.pcps.main.model.enums.ProcessMessageEnum;
import dk.apps.pcps.main.model.payload.ReqLogon;
import dk.apps.pcps.main.model.payload.ReqUpdateBalance;
import dk.apps.pcps.main.model.payload.RespUnPair;
import dk.apps.pcps.main.model.payload.TrxHistoryData;
import dk.apps.pcps.main.model.result.*;
import dk.apps.pcps.main.module.tapcash.model.*;
import dk.apps.pcps.main.module.tapcash.model.enums.MTIActionEnum;


import dk.apps.pcps.dbmaster.entity.MobileAppUsers;
import dk.apps.pcps.dbmaster.service.MobileAppUsersService;


import dk.apps.pcps.main.module.tapcash.config.Channel;
import dk.apps.pcps.main.module.tapcash.config.HostService;

import dk.apps.pcps.main.utils.DateTimeUtils;
import dk.apps.pcps.main.utils.ISOConverters;
import dk.apps.pcps.main.utils.ObjectMapper;
import dk.apps.pcps.main.utils.Utility;
import lombok.extern.slf4j.Slf4j;
import org.jpos.iso.ISOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class TapCashImpl implements TapCashService {

    AuthService authService;
    BatchGroupService batchGroupService;
    Channel channel;
    TrxChannelService trxChannelService;
    HostService hostService;
    MobileAppUsersService mobileAppUsersService;
    DeviceUserDetailService deviceUserDetailService;
    DeviceUserAndBatchGroupService deviceUserAndBatchGroupService;
    MerchantAndBatchGroupService merchantAndBatchGroupService;
    SuccessPaymentService successPaymentService;
    RefundService refundService;
    SettlementDataViewService settlementDataViewService;
    SettlementService settlementService;
    TrxHistoryService trxHistoryService;
    CardUpdateHistoryService cardUpdateHistoryService;
    ReversalCardUpdateService reversalCardUpdateService;
    SettlementFileUploadService settlementFileUploadService;

    @Autowired
    public TapCashImpl(AuthService authService,
                       BatchGroupService batchGroupService,
                       Channel channel,
                       HostService hostService,
                       MobileAppUsersService mobileAppUsersService,
                       DeviceUserAndBatchGroupService deviceUserAndBatchGroupService,
                       MerchantAndBatchGroupService merchantAndBatchGroupService,
                       SuccessPaymentService successPaymentService,
                       RefundService refundService,
                       SettlementDataViewService settlementDataViewService,
                       CardUpdateHistoryService cardUpdateHistoryService,
                       ReversalCardUpdateService reversalCardUpdateService,
                       SettlementFileUploadService settlementFileUploadService
    ) {
        this.authService = authService;
        this.batchGroupService = batchGroupService;
        this.channel = channel;
        this.hostService = hostService;
        this.mobileAppUsersService = mobileAppUsersService;
        this.deviceUserAndBatchGroupService = deviceUserAndBatchGroupService;
        this.merchantAndBatchGroupService = merchantAndBatchGroupService;
        this.successPaymentService = successPaymentService;
        this.refundService = refundService;
        this.settlementDataViewService = settlementDataViewService;
        this.cardUpdateHistoryService = cardUpdateHistoryService;
        this.reversalCardUpdateService = reversalCardUpdateService;
        this.settlementFileUploadService = settlementFileUploadService;

    }

    private String getUsername(){
        MobileAppUsers mobileAppUsers = authService.getAuthUsers();
        return mobileAppUsers.getUsername();
    }

    private BatchGroup getBatchGroup(int batchGroupId){
        BatchGroup batchGroup = batchGroupService.getBatchGroup(1);
        return batchGroup;
    }

    private TapCashConfig getConfig(){
        BatchGroup batchGroup = getBatchGroup(1);
        String adParam = batchGroup.getAdditionalParam();
        return Utility.jsonToObject(adParam, TapCashConfig.class);
    }

    @Override
    public void saveTMK(int batchGroupId, String seed, String tmk, String mtmkIndex) {
        MobileAppUsers mobileAppUsers = authService.getAuthUsers();
        String username = mobileAppUsers.getUsername();
        CardEnum card = CardEnum.getCardEnum(batchGroupId);
        String addData = null;
        switch (card) {
            case TAPCASH:
                LogonData logonData = Utility.mapperToDto(deviceUserAndBatchGroupService.getAdditionalData(username, getBatchGroup(batchGroupId)), LogonData.class);
                logonData.setSeed(seed);
                logonData.setTmk(tmk);
                logonData.setMtmkIndex(mtmkIndex);
                addData = Utility.objectToString(logonData);
                break;
        }
        deviceUserAndBatchGroupService.createAdditionalData(username, getBatchGroup(batchGroupId), addData);
    }

    @Override
    public Boolean logoff(ReqLogon req) {
        Boolean isLogon;
        Messages msg = new Messages();
        msg.setMID(req.getMid());
        msg.setUsername(getUsername());
        msg.setMID(req.getMid());
        msg.setTID(req.getTid());
        AdditionalData ad = new AdditionalData();
        ad.setSoftwareVersion("0100");
        msg.setAdditionalData(ad);
        try{
            msg = hostService.requestToHost(CardEnum.TAPCASH, msg, MTIActionEnum.LOGOFF);
            isLogon = msg.getResponseCode().equals("00");
        } catch (ISOException | InterruptedException ex){
            throw new ApplicationException(ProcessMessageEnum.FAILED_LOGOFF);
        }
        return isLogon;
    }

    @Override
    public LogonData logonProcess(ReqLogon req, boolean isSettlement) {
        String username = getUsername();
        String mid = req.getMid();
        String tid = req.getTid();
        Messages msg = new Messages();
        msg.setUsername(username);
        msg.setMID(mid);
        msg.setTID(tid);
        CardEnum card = CardEnum.TAPCASH;
        TrxChannel trxChannel = channel.getChannel(card);
        BatchGroup batchGroup = trxChannel.getBatchGroup();
        LogonData logonData = Utility.mapperToDto(deviceUserAndBatchGroupService.getAdditionalData(username, batchGroup), LogonData.class);
        String softwareVersion = "0100";
        //Logoff
        try {

            AdditionalData ad = new AdditionalData();
            if (isSettlement) {
                ad.setSoftwareVersion(softwareVersion);
                msg.setAdditionalData(ad);
                hostService.requestToHost(card, msg, MTIActionEnum.LOGOFF);
            }
            //Initialization
            ad = new AdditionalData();
            ad.setSoftwareVersion(softwareVersion);
            ad.setSeed(logonData.getSeed());
            ad.setCurrentBlacklistVersion("000000");
            ad.setMtmkIndex(logonData.getMtmkIndex());
            msg.setAdditionalData(ad);
            Messages msgInit = hostService.requestToHost(card, msg, MTIActionEnum.INITIALIZATION);
            String blacVersion = msgInit.getAdditionalData().getBlacklistVersionFrom();
            String paraVersion = msgInit.getAdditionalData().getParameterVersion();
            ad = msgInit.getAdditionalData();
            logonData.setRn(ad.getRn());
            logonData.setKek1(ad.getKek1());
            logonData.setMack(ad.getMack());
            deviceUserAndBatchGroupService.createAdditionalData(username, batchGroup, Utility.objectToString(logonData));
            if (logonData.getParameterVersion() == null || !logonData.getParameterVersion().equalsIgnoreCase(paraVersion)) {
                //Parameter
                ad = new AdditionalData();
                ad.setSoftwareVersion(softwareVersion);
                ad.setParameterVersion(paraVersion);
                ad.setFileName("PARA");
                ad.setRecordNumber("0000");
                msg.setAdditionalData(ad);
                Messages msgParam = hostService.requestToHost(card, msg, MTIActionEnum.PARAMETER);
                logonData.setParameterVersion(paraVersion);
                logonData.setParameterData(msgParam.getAdditionalData().getFileData());
            }
            if (logonData.getBlacklistVersion() == null || !logonData.getBlacklistVersion().equalsIgnoreCase(blacVersion)) {
                //Blacklist
                Messages msgBlac;
                String next;
                int i = 0;
                List<Object> lst = new ArrayList<>();
                AdditionalData additionalData = new AdditionalData();
                do {
                    next = ISOConverters.padLeftZeros("" + i * 10, 4);
                    ad = new AdditionalData();
                    ad.setSoftwareVersion(softwareVersion);
                    ad.setBlacklistVersion(blacVersion);
                    ad.setFileName("BLAC");
                    ad.setRecordNumber(next);
                    msg.setAdditionalData(ad);
                    msgBlac = hostService.requestToHost(card, msg, MTIActionEnum.BLACKLIST);
                    if (msgBlac != null) {
                        additionalData = msgBlac.getAdditionalData();
                        if (msgBlac.getAdditionalData() != null) {
                            List<FileData> fileData = msgBlac.getAdditionalData().getFileData();
                            lst.addAll(fileData);
                        }
                    }
                    i++;
                } while (additionalData != null);

                BlacklistData blacklist = new BlacklistData();
                blacklist.setNextRecordNumber(next);
                blacklist.setFileData(lst);
                logonData.setBlacklistData(blacklist);
                logonData.setBlacklistVersion(blacVersion);
            }
            //Logon
            ad = new AdditionalData();
            ad.setSoftwareVersion(softwareVersion);
            msg.setAdditionalData(ad);
            hostService.requestToHost(card, msg, MTIActionEnum.LOGON);
            deviceUserAndBatchGroupService.createAdditionalData(username, batchGroup, Utility.objectToString(logonData));
            return logonData;
        } catch (ISOException | InterruptedException e) {
            return null;
        }
    }


    @Override
    @Transactional
    public SettlementSummaryResult doSettlement(String username) {
        List<SettlementSessionData> settlementSessionDataList = new ArrayList<>();
        List<BatchGroupData> batchGroupDataList = new ArrayList<>();
        int sessNum = deviceUserDetailService.getSessionNumber(username);
        List<BatchGroup> batchGroups = settlementDataViewService.getBatchGroup(username);
        if (batchGroups.size() == 0)
            throw new ApplicationException(ProcessMessageEnum.NOT_FOUND, "settlement data");
        for (BatchGroup batchGroup : batchGroups) {
            TrxChannel trxChannel = trxChannelService.getTrxChannel(batchGroup.getId());
            batchGroupDataList.add(settlementSession(username, batchGroup, sessNum, false).setHost(trxChannel.getNii()));
        }
        settlementSessionDataList.add(new SettlementSessionData().setSessionNumber(sessNum).setSettlementAt(Utility.getTimeStamp()).setSettlementSummaryList(batchGroupDataList));
        SettlementSummaryResult settlementResult = new SettlementSummaryResult().setSettlementSessionList(settlementSessionDataList);
        log.info(Utility.objectToString(settlementResult));
        deviceUserDetailService.updateSessionNumber(username, sessNum);
        cardUpdateHistoryService.updateCardUpdateHistory(username, sessNum);
        return settlementResult;
    }

    @Override
    public List<SettlementMessage> generateSettlementMessages(int sessionNum) {
        List<SettlementMessage> settlementMessages = new ArrayList<>();
        String username = getUsername();
        BatchGroup batchGroup = getBatchGroup(1);
        int countUpload = 8;
        String mid = "";
        String tid = "";
        List<Map> fileDataPage = new ArrayList<>();
        Map mCount = settlementDataViewService.countSettlementData(batchGroup, username, countUpload);
        int cPage = (int) mCount.get("total_page");
        int batchNumber = deviceUserAndBatchGroupService.getBatchNumber(username, batchGroup);
        for (int i = 0; i < cPage; i++) {
            int batchSeqNo = i + 1;
            int x = 0;
            int numOfTrx = 0;
            long totalPaymentAmount = 0;
            List<FileData> fileDataList = new ArrayList<>();
            List<SettlementDataView> settlementDataViews = settlementDataViewService.getDataForSettlement(username, i + 1, countUpload, "DESC:createAt");
            Integer[] invoiceNumbers = new Integer[settlementDataViews.size()];
            for (SettlementDataView result : settlementDataViews) {
                mid = result.getMid();
                tid = result.getTid();
                CardData cardData = Utility.jsonToObject(result.getAdditionalData(), CardData.class);
                FileData fileData = new FileData();
                fileData.setCan(result.getCan());
                fileData.setTransHeader(cardData.getTransHeader());
                fileData.setTransType(cardData.getTransType());
                fileData.setTransData(cardData.getTransData());
                fileData.setLastPurseStatus(cardData.getPurseStatus());
                fileData.setLastCreditTRP(cardData.getLastCreditTrp());
                fileData.setLastCreditHeader(cardData.getLastCreditHeader());
                fileData.setLastPurseBalance(cardData.getPurseBalance());
                fileData.setLastTransTRP(cardData.getLastTxnTrp());
                fileData.setLastTransRecord(cardData.getLastTxnRecord());
                fileData.setLastBDC(cardData.getBdc());
                fileData.setLastDebitOption(cardData.getDebitOption());
                fileData.setLastTransSignCert(cardData.getLastTransSignCert());
                fileData.setLastCounterData(cardData.getLastCounterData());
                fileData.setInvoiceReference("202020202020202020202020202020");
                fileDataList.add(fileData);
                invoiceNumbers[x++] = result.getInvoiceNum();
            }

            Messages messages = new Messages();
            messages.setTID(tid);
            messages.setMID(mid);
            AdditionalData additionalData = new AdditionalData();
            additionalData.setSoftwareVersion("0200");
            additionalData.setBatchNo(ISOConverters.padLeftZeros(String.valueOf(batchNumber), 6));
            additionalData.setBatchSeqNo(ISOConverters.padLeftZeros("" + batchSeqNo, 2));
            additionalData.setEndOfBatch(ISOConverters.padLeftZeros(String.valueOf(fileDataPage.size()), 2));
            additionalData.setNumberOfTrans(ISOConverters.padLeftZeros(String.valueOf(numOfTrx), 2));
            additionalData.setFileData(fileDataList);
            additionalData.setInvoiceNumbers(invoiceNumbers);
            messages.setAdditionalData(additionalData);
            messages.setUsername(username);
            SettlementMessage settlementMessage = new SettlementMessage();
            settlementMessage.setInvoiceNums(invoiceNumbers);
            settlementMessage.setTid(tid);
            settlementMessage.setMid(mid);
            settlementMessage.setNumOfTrx(numOfTrx);
            settlementMessage.setTotalAmount(totalPaymentAmount);
            settlementMessage.setMessages(messages);
            settlementMessages.add(settlementMessage);
            try {
                hostService.requestToHost(CardEnum.TAPCASH, messages, MTIActionEnum.FILE_UPLOAD);
            } catch (ISOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return settlementMessages;
    }

    @Override
    public List<Map> generateSettlementData(int countUpload) {
        String mid = "";
        String tid = "";
        String username = getUsername();
        BatchGroup batchGroup = getBatchGroup(1);
        List<Map> fileDataPage = new ArrayList<>();
        Map mCount = settlementDataViewService.countSettlementData(batchGroup, username, countUpload);
        int cPage = (int) mCount.get("total_page");
        for (int i = 0; i < cPage; i++) {
            int page = i + 1;
            int x = 0;
            int paymentCount = 0;
            int refundCount = 0;
            long totalPaymentAmount = 0;
            long totalRefundAmount = 0;
            Map mFileDataPage = new HashMap();
            List<FileData> fileDataList = new ArrayList<>();
            StringBuffer sbBody = new StringBuffer();
            List<SettlementDataView> successPayments = settlementDataViewService.getDataForSettlement(batchGroup, username, page, countUpload, "DESC:createAt");
            Integer[] invoiceNumbers = new Integer[successPayments.size()];
            StringBuffer sbINums = new StringBuffer();
            for (SettlementDataView settlement: successPayments){
                mid = successPayments.get(x).getMid();
                tid = successPayments.get(x).getTid();
                CardData cardData = Utility.jsonToObject(settlement.getAdditionalData(), CardData.class);
                if (cardData.getCan() != null) {
                    sbINums.append(ISOConverters.padLeftZeros(String.valueOf(settlement.getInvoiceNum()), 6));
                    sbBody.append(cardData.getStringData()).append("\n");
                    totalPaymentAmount += settlement.getBaseAmount();
                }
            }

            if (!sbBody.toString().isEmpty()) {
                StringBuffer sbFileName = new StringBuffer();
                sbFileName.append(mid); //mid
                sbFileName.append(tid); //tid
                sbFileName.append(DateTimeUtils.getDateTime("yyyyMMdd")); //date
                sbFileName.append(DateTimeUtils.getDateTime("hhmmss")); //time
                sbFileName.append(".txt"); //suffix
                StringBuffer sbHeader = new StringBuffer();
                sbHeader.append("H");//header indicator
                sbHeader.append("01");//version
                sbHeader.append(mid);//mid
                sbHeader.append(tid);//tid

                StringBuffer sbSettlement = new StringBuffer();
                sbSettlement.append(sbHeader);
                sbSettlement.append("\n");
                sbSettlement.append(sbBody);

                Map mSettlement = new HashMap();
                mSettlement.put("file_name", sbFileName);
                mSettlement.put("content", sbSettlement);

                SettlementFileUpload data = new SettlementFileUpload();
                data.setBatchGroupId(1);
                data.setPage(page);
                data.setUsername(getUsername());
                data.setFileName(sbFileName.toString());
                data.setHeader(sbHeader.toString());
                data.setBody(sbBody.toString());
                data.setStatus("PENDING");
                data.setInvoiceNums(sbINums.toString());
                data.setTotalAmount(totalPaymentAmount);
                settlementFileUploadService.createData(data);
                fileDataPage.add(mSettlement);
            }
        }
        return fileDataPage;
    }

    @Override
    public boolean uploadSettlementData() {
        return false;
    }

    @Override
    public boolean checkSettlement() {
        return false;
    }


    @Override
    public InquiryUpdateBalanceResult updateBalanceInquiry(ReqUpdateBalance req) {
        MobileAppUsers mobileAppUsers = mobileAppUsersService.getUser(req.getUsername());
        InquiryUpdateBalanceResult result = null;
        BatchGroup batchGroup = new BatchGroup();
        batchGroup.setId(1);
        DeviceUserAndBatchGroup deviceUserAndBatchGroup = deviceUserAndBatchGroupService.getDeviceUserAndBatchGroup(req.getUsername(), batchGroup);
        String tid = deviceUserAndBatchGroup.getTid();
        MerchantAndBatchGroup merchantAndBatchGroup = merchantAndBatchGroupService.getMerchantAndBatchGroup(mobileAppUsers.getMerchant().getId(), batchGroup);
        String mid = merchantAndBatchGroup.getMid();
        try {
            CardDataUpdate cardData = Utility.mapperToDto(req.getCardData(), CardDataUpdate.class);
            AdditionalData additionalData = new AdditionalData();
            additionalData.setSoftwareVersion("0100");
            additionalData.setCAN(req.getCan());
            additionalData.setCSN(cardData.getCsn());
            additionalData.setTRN(cardData.getTrn());
            additionalData.setCrn(cardData.getCrn());
            additionalData.setTRP(cardData.getTrp());
            additionalData.setLastPurseStatus(cardData.getLastPurseStatus());
            additionalData.setLastPurseBalance(cardData.getLastPurseBalance());
            additionalData.setLastCreditTRP(cardData.getLastCreditTrp());
            additionalData.setLastCreditHeader(cardData.getLastCreditHeader());
            additionalData.setLastTransTRP(cardData.getLastTransTrp());
            additionalData.setLastTransRecord(cardData.getLastTransRecord());
            additionalData.setLastBDC(cardData.getLastBdc());
            additionalData.setLastDebitOption(cardData.getLastDebitOption());
            additionalData.setLastTransSignCert(cardData.getLastTransSignCert());
            additionalData.setLastCounterData(cardData.getLastCounterData());
            Messages messages = new Messages();
            messages.setPAN(req.getCan());
            messages.setMID(mid);
            messages.setTID(tid);
            messages.setUsername(req.getUsername());
            messages.setAdditionalData(additionalData);
            result = new InquiryUpdateBalanceResult();
            MTIActionEnum mti = MTIActionEnum.CARD_UPDATE;
            boolean isReversal = req.isReversal();
            if (isReversal) {
                mti = MTIActionEnum.CARD_UPDATE_REVERSAL;
                messages.setSTAN(req.getStan());
            }
            messages = hostService.requestToHost(CardEnum.TAPCASH, messages, mti, isReversal);
            String stan = messages.getSTAN();
            result.setStan(stan);
            result.setRrn(messages.getRRN());
            result.setReversal(isReversal);
            if (!isReversal) {
                result.setTransactionLogRecord(messages.getAdditionalData().getTransLogRecord());
                result.setCreditCryptogram(messages.getAdditionalData().getCreditCryptogram());
                byte[] logRecord = ISOConverters.hexStringToBytes(messages.getAdditionalData().getTransLogRecord());
                TransactionLogResult trxLog = new TransactionLogResult().setType(ISOConverters.bytesToHex(Arrays.copyOfRange(logRecord, 0, 1)))
                        .setAmount(ISOConverters.bytesToHex(Arrays.copyOfRange(logRecord, 1, 4)))
                        .setTxnDateTime(ISOConverters.bytesToHex(Arrays.copyOfRange(logRecord, 4, 8)))
                        .setUserData(ISOConverters.bytesToHex(Arrays.copyOfRange(logRecord, 8, logRecord.length)));
                result.setTransactionLogResult(trxLog);
                CardUpdateHistory cardUpdateHistory = new CardUpdateHistory();
                cardUpdateHistory.setUsername(mobileAppUsers.getUsername());
                cardUpdateHistory.setAdditionalData(Utility.objectToString(trxLog));
                cardUpdateHistoryService.createCardUpdateHistory(cardUpdateHistory);
            } else {
                log.info("Card Update is Reversal");
                this.successCardUpdate();
                return null;
            }
            this.createReversalCardUpdate(stan, req);
            CompletableFuture.runAsync(() -> this.cardUpdateReversal(req));
        } catch (ISOException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public SamData createOrUpdateMarryCode(String marriageCode) {
        MobileAppUsers mobileAppUsers = authService.getAuthUsers();
        String username = mobileAppUsers.getUsername();
        BatchGroup batchGroup = getBatchGroup(1);
        String addData = null;
        SamData samData = new SamData().setPairCode(marriageCode);
        LogonData logonData = Utility.mapperToDto(deviceUserAndBatchGroupService.getAdditionalData(username, batchGroup), LogonData.class);
        logonData.setSamData(samData);
        addData = Utility.objectToString(logonData);
        deviceUserAndBatchGroupService.createAdditionalData(username, batchGroup, addData);
        return samData;
    }

    @Override
    public void unMarrySuccess(String unPairCode, String extAuthCode) {
        MobileAppUsers mobileAppUsers = authService.getAuthUsers();
        String username = mobileAppUsers.getUsername();
        BatchGroup batchGroup = batchGroupService.getBatchGroup(1);
        String addData = null;
        LogonData logonData = Utility.mapperToDto(deviceUserAndBatchGroupService.getAdditionalData(username, batchGroup), LogonData.class);
        SamData samData = logonData.getSamData();
        if(samData.getUnPairCode().equals(unPairCode) && samData.getExtAuthCode().equals(extAuthCode)){
            logonData.setSamData(null);
            addData = Utility.objectToString(logonData);
        }
        if (addData == null)
            throw new ApplicationException(ProcessMessageEnum.FAILED_UPDATED);
        deviceUserAndBatchGroupService.createAdditionalData(username, batchGroup, addData);
    }

    private void successCardUpdate() {
        MobileAppUsers appUsers = authService.getAuthUsers();
        String username = appUsers.getUsername();
        ReversalCardUpdateData data = reversalCardUpdateService.getReversal(username);
        if (data == null)
            throw new ApplicationException(ProcessMessageEnum.NOT_FOUND);
        reversalCardUpdateService.removeReversal(username);
    }

    private void createReversalCardUpdate(String stan, ReqUpdateBalance req) {
        MobileAppUsers appUsers = authService.getAuthUsers();
        String username = appUsers.getUsername();
        req.setUsername(username);
        req.setReversal(true);
        req.setStan(stan);
        reversalCardUpdateService.createReversal(new ReversalCardUpdateData()
                .setUsername(username)
                .setAdditionalData(Utility.objectToString(req))
        );
    }

    @Override
    @Async("reversalExecutor")
    public InquiryUpdateBalanceResult cardUpdateReversal(ReqUpdateBalance req) {
        try {
            Thread.sleep(PCPSConfig.REVERSAL_DELAY);
        } catch (InterruptedException e) {
            log.error("reversalExecutor-" + e.getMessage());
        }
        ReversalCardUpdateData data = reversalCardUpdateService.getReversal(req.getUsername());
        if (data == null)
            return null;
        return updateBalanceInquiry(req);
    }

    @Override
    public RespUnPair getUnMarryCode(String samId, String crn, String trn) {
        MobileAppUsers mobileAppUsers = authService.getAuthUsers();
        String username = mobileAppUsers.getUsername();
        BatchGroup batchGroup = new BatchGroup();
        batchGroup.setId(1);
        DeviceUserAndBatchGroup deviceUserAndBatchGroup = deviceUserAndBatchGroupService.getDeviceUserAndBatchGroup(username, batchGroup);
        MerchantAndBatchGroup merchantAndBatchGroup = merchantAndBatchGroupService.getMerchantAndBatchGroup(mobileAppUsers.getMerchant().getId(), batchGroup);
        String unPairCode = null;
        String extAuthCode = null;
        String addData = null;

        try {
            Messages msg = new Messages();
            msg.setUsername(username);
            msg.setTID(deviceUserAndBatchGroup.getTid());
            msg.setMID(merchantAndBatchGroup.getMid());
            AdditionalData ad = new AdditionalData();
            ad.setSamId(samId);
            ad.setCrn(crn);
            ad.setRrn(trn);
            msg.setAdditionalData(ad);
            msg = hostService.requestToHost(CardEnum.TAPCASH, msg, MTIActionEnum.UN_MARRY_SAM);
            unPairCode = msg.getAdditionalData().getUnMarryCode();
            extAuthCode = msg.getAdditionalData().getExternalAuthCode();
            LogonData logonData = Utility.mapperToDto(deviceUserAndBatchGroupService.getAdditionalData(username, batchGroup), LogonData.class);
            SamData samData = logonData.getSamData();
            if (samData == null) {
                samData = new SamData();
            }
            samData.setUnPairCode(unPairCode);
            samData.setExtAuthCode(extAuthCode);
            logonData.setSamData(samData);
            addData = Utility.objectToString(logonData);

            deviceUserAndBatchGroupService.createAdditionalData(username, batchGroup, addData);
            return new RespUnPair().setUnPairCode(unPairCode).setExtAuth(extAuthCode);
        } catch (ISOException | InterruptedException e) {
            return null;
        }

    }

    @Override
    public RespUnPair generateUnMarryCode(String samId, String crn, String trn) {
        String username = getUsername();
        BatchGroup batchGroup = getBatchGroup(1);
        String samKey = getConfig().getSamKey();
        byte[] baSamKey = Utility.hexStringToByteArray(samKey);
        byte[] baSamID = Utility.hexStringToByteArray(samId);
        byte[] baR1 = Utility.hexStringToByteArray(crn);
        byte[] baR2 = Utility.hexStringToByteArray(trn);
        byte[] baR1r2 = ByteBuffer.allocate(baR1.length + baR2.length).put(baR1).put(baR2).array();
        byte[] baExtAuth = TripleDESFunction.encrypt3DES(baR1r2, baSamKey);
        byte[] baR1L = Arrays.copyOfRange(baR1, 0, 4);
        byte[] baR1R = Arrays.copyOfRange(baR1, 4, baR1.length);
        byte[] baR2L = Arrays.copyOfRange(baR2, 0, 4);
        byte[] baR2R = Arrays.copyOfRange(baR2, 4, baR2.length);
        byte[] mixCrnTrn = ByteBuffer.allocate(baR1L.length + baR2L.length + baR1R.length + baR2R.length)
                .put(baR1L).put(baR2L).put(baR1R).put(baR2R).array();
        byte[] baSkKey = TripleDESFunction.encrypt3DES(mixCrnTrn, baSamKey);
        byte[] baUnMarryCode = TripleDESFunction.encrypt3DES(baSamID, baSkKey);
        String extAuth = Utility.bytesToHex(baExtAuth);
        String unMarryCode = Utility.bytesToHex(baUnMarryCode);
        LogonData logonData = Utility.mapperToDto(deviceUserAndBatchGroupService.getAdditionalData(username, batchGroup), LogonData.class);
        SamData samData = logonData.getSamData();
        if(samData == null){
            samData = new SamData();
        }
        samData.setUnPairCode(unMarryCode);
        samData.setExtAuthCode(extAuth);
        logonData.setSamData(samData);
        String addData = Utility.objectToString(logonData);
        deviceUserAndBatchGroupService.createAdditionalData(username, batchGroup, addData);
        return new RespUnPair().setUnPairCode(Utility.bytesToHex(baUnMarryCode)).setExtAuth(Utility.bytesToHex(baExtAuth));
    }

    private Messages sendSettlement(CardEnum card, Messages messages) {
        try {
            Messages msg = null;
            switch (card) {
                case TAPCASH:
                    msg = hostService.requestToHost(card, messages, MTIActionEnum.FILE_UPLOAD);
                    break;
                default:
                    break;
            }
            return msg;
        } catch (ISOException | InterruptedException e) {

        }
        return null;
    }

    private String getPaidAt(CardEnum card, String addData) {
        String hexDate = null;
        switch (card) {
            case TAPCASH:
                CardData cardData = Utility.jsonToObject(addData, CardData.class);
                hexDate = cardData.getTxnDateTime();
                break;
            default:
                break;
        }
        return hexDate;
    }


    private BatchGroupData settlementSession(String username, BatchGroup batchGroup, int sessNum, boolean flag) {
        log.info(username + " settlement is starting");
        int countUpload = 8;
        String mid = "";
        String tid = "";
        boolean isSettlement = false;
        String hostDate = null, hostTime = null;
        Settlement reqSettlement = null;
        List<SettlementDetail> settlementDetails = new ArrayList<>();
        List<Map> fileDataPage = new ArrayList<>();
        List<Integer> lstRefundId = new ArrayList<>();
        List<Integer> lstInvoiceNums = new ArrayList<>();
        List<SettlementDataView> settlementDataHistorys = new ArrayList<>();
        int batchGroupId = batchGroup.getId();
        String batchGroupName = batchGroup.getName();
        CardEnum card = CardEnum.getCardEnum(batchGroupId);
        Map mCount = settlementDataViewService.countSettlementData(batchGroup, username, countUpload);
        int cPage = (int) mCount.get("total_page");
        int batchNumber = deviceUserAndBatchGroupService.getBatchNumber(username, batchGroup);
        for (int i = 0; i < cPage; i++) {
            int x = 0;
            int paymentCount = 0;
            int refundCount = 0;
            long totalPaymentAmount = 0;
            long totalRefundAmount = 0;
            Map mFileDataPage = new HashMap();
            List<FileData> fileDataList = new ArrayList<>();
            List<SettlementDataView> settlementDataViews = settlementDataViewService.getDataForSettlement(username, i + 1, countUpload, "DESC:createAt");
            Integer[] invoiceNumbers = new Integer[settlementDataViews.size()];
            for (SettlementDataView result : settlementDataViews) {
                mid = result.getMid();
                tid = result.getTid();
                if (result.getTrxType().equals("PAYMENT")) {
                    paymentCount = paymentCount + 1;
                    totalPaymentAmount = totalPaymentAmount + result.getBaseAmount();
                    lstInvoiceNums.add(result.getInvoiceNum());
                } else if (result.getTrxType().equals("REFUND")) {
                    refundCount = refundCount + 1;
                    totalRefundAmount = totalRefundAmount + result.getBaseAmount();
                    lstRefundId.add(result.getId());
                    lstInvoiceNums.add(result.getInvoiceNum());
                }

                CardData cardData = Utility.jsonToObject(result.getAdditionalData(), CardData.class);
                FileData fileData = new FileData();
                fileData.setCan(result.getCan());
                fileData.setTransHeader(cardData.getTransHeader());
                fileData.setTransType(cardData.getTransType());
                fileData.setTransData(cardData.getTransData());
                fileData.setLastPurseStatus(cardData.getPurseStatus());
                fileData.setLastCreditTRP(cardData.getLastCreditTrp());
                fileData.setLastCreditHeader(cardData.getLastCreditHeader());
                fileData.setLastPurseBalance(cardData.getPurseBalance());
                fileData.setLastTransTRP(cardData.getLastTxnTrp());
                fileData.setLastTransRecord(cardData.getLastTxnRecord());
                fileData.setLastBDC(cardData.getBdc());
                fileData.setLastDebitOption(cardData.getDebitOption());
                fileData.setLastTransSignCert(cardData.getLastTransSignCert());
                fileData.setLastCounterData(cardData.getLastCounterData());
                fileData.setInvoiceReference("202020202020202020202020202020");
                fileDataList.add(fileData);
                invoiceNumbers[x++] = result.getInvoiceNum();
                mFileDataPage.put("file_data_list", fileDataList);


            }
            mFileDataPage.put("invoice_numbers", invoiceNumbers);
            mFileDataPage.put("tid", tid);
            mFileDataPage.put("mid", mid);
            mFileDataPage.put("payment_count", paymentCount);
            mFileDataPage.put("total_amount", totalPaymentAmount);
            mFileDataPage.put("trx_historys", settlementDataViews);
            fileDataPage.add(mFileDataPage);
            settlementDataHistorys.addAll(settlementDataViews);
        }

        int totalPageTransaction = 0;
        long totalPageAmount = 0;
        List<Integer> invNums = new ArrayList<>();
        TrxChannel trxChannel = null;
        for (int i = 0; i < fileDataPage.size(); i++) {
            int batchSeqNo = i + 1;
            tid = (String) fileDataPage.get(i).get("tid");
            mid = (String) fileDataPage.get(i).get("mid");
            Integer[] invoiceNumbers = (Integer[]) fileDataPage.get(i).get("invoice_numbers");
            Messages resp;
            Messages messages = new Messages();
            messages.setTID(tid);
            messages.setMID(mid);
            AdditionalData additionalData = new AdditionalData();
            additionalData.setSoftwareVersion("0200");
            additionalData.setBatchNo(ISOConverters.padLeftZeros(String.valueOf(batchNumber), 6));
            additionalData.setBatchSeqNo(ISOConverters.padLeftZeros("" + batchSeqNo, 2));
            additionalData.setEndOfBatch(ISOConverters.padLeftZeros(String.valueOf(fileDataPage.size()), 2));
            additionalData.setNumberOfTrans(ISOConverters.padLeftZeros(String.valueOf(fileDataPage.get(i).get("payment_count")), 2));
            additionalData.setFileData((List<FileData>) fileDataPage.get(i).get("file_data_list"));
            additionalData.setInvoiceNumbers(invoiceNumbers);
            messages.setAdditionalData(additionalData);
            messages.setUsername(username);


            String stan;
            resp = sendSettlement(card, messages);
            if (resp != null) {
                if (!resp.getResponseCode().equals("00")) {
                    throw new MessageException(resp.getResponseCode());
                }
                int trxCount = (int) fileDataPage.get(i).get("payment_count");
                long trxTotalAmount = (long) fileDataPage.get(i).get("total_amount");
                if (resp.getResponseCode().equals("00") && resp.getMTI().equals("0310")) {
                    stan = resp.getSTAN();
                    hostDate = resp.getLocalDate();
                    hostTime = resp.getLocalTime();
                    totalPageTransaction += trxCount;
                    totalPageAmount += trxTotalAmount;
                    trxChannel = resp.getTrxChannel();
                    SettlementDetail settlementDetail = new SettlementDetail()
                            .setBatchNumber(ISOConverters.padLeftZeros("" + batchNumber, 6))
                            .setStan(ISOConverters.padLeftZeros(stan, 6))
                            .setNumOfTransaction(totalPageTransaction)
                            .setTotalBaseAmount(totalPageAmount);
                    settlementDetails.add(settlementDetail);
                    isSettlement = true;
                }
            }
            invNums.addAll(Arrays.asList(invoiceNumbers));
            logonProcess(new ReqLogon().setBatchGroupId(batchGroupId).setMid(mid).setTid(tid), true);
        }

        if (isSettlement) {
            reqSettlement = new Settlement();
            reqSettlement.setUsername(username);
            reqSettlement.setBatchGroup(batchGroup);
            reqSettlement.setBatchNum(batchNumber);
            reqSettlement.setAdditionalParam(Utility.objectToString(settlementDetails));
            reqSettlement.setNumOfSuccessPayment(totalPageTransaction);
            reqSettlement.setSuccessPaymentTotalBaseAmount(totalPageAmount);
            reqSettlement.setNumOfRefund(0);
            reqSettlement.setRefundTotalBaseAmount(0);
            reqSettlement.setNumOfTips(0);
            reqSettlement.setTipsTotalBaseAmount(0);
            reqSettlement.setSettlementSessionNum(0);
            reqSettlement.setSettlementHostDate(hostDate);
            reqSettlement.setSettlementHostTime(hostTime);
            reqSettlement.setSettlementSessionNum(sessNum);
            reqSettlement.setTrxChannel(trxChannel);
            Settlement settlement = settlementService.createDataSettlement(reqSettlement);
            DeviceUserAndBatchGroup reqDeviceUserAndBatchGroup = new DeviceUserAndBatchGroup();
            reqDeviceUserAndBatchGroup.setUsername(username);
            reqDeviceUserAndBatchGroup.setBatchNum(batchNumber);
            if (settlement != null) {
                List<TrxHistoryData> trxHistoryDataList = ObjectMapper.mapAll(settlementDataHistorys, TrxHistoryData.class);
                int finalBatchNumber = batchNumber;
//                String finalStan = stan;
                trxHistoryDataList.forEach(
                        f -> {
                            Timestamp paidAt = Timestamp.valueOf(ISOConverters.hexToDate(getPaidAt(card, f.getAdditionalData())));
                            f.setBatchNum(finalBatchNumber);
                            f.setMaskedCan(Utility.maskedCan(f.getCan()));
                            f.setPaidAt(paidAt);
//                            f.setStan(finalStan);
                        }
                );
                List<TrxHistory> trxHistories = ObjectMapper.mapAll(trxHistoryDataList, TrxHistory.class);
                trxHistories = trxHistoryService.createTrxHistory(trxHistories);
                if (trxHistories.size() > 0) {
                    if (invNums.size() > 0) {
                        successPaymentService.removeAllDataBatch(invNums);
                        deviceUserAndBatchGroupService.updateBatchNumber(username, batchGroup, batchNumber);
                    }
                    if (lstRefundId.size() > 0 && lstInvoiceNums.size() > 0) {
                        refundService.removeAllDataBatch(lstRefundId);
                        successPaymentService.removeAllDataBatch(lstInvoiceNums);
                    }
                }
            }
        } else {
            log.info("settlement failed from host");
            return null;
        }
        log.info(username + " settlement is success");
        return new BatchGroupData()
                .setId(batchGroupId)
                .setMid(mid)
                .setTid(tid)
                .setName(batchGroupName)
                .setTrxCount(totalPageTransaction)
                .setTotalAmount(totalPageAmount);
    }
}
