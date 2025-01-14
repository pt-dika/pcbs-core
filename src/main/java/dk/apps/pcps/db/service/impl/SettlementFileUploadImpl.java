package dk.apps.pcps.db.service.impl;

import dk.apps.pcps.config.model.Sftp;
import dk.apps.pcps.config.sftp.FileService;
import dk.apps.pcps.config.sftp.FileTransferService;
import dk.apps.pcps.db.entity.*;
import dk.apps.pcps.db.service.*;
import dk.apps.pcps.main.model.result.BatchGroupData;
import dk.apps.pcps.main.model.result.SettlementSessionData;
import dk.apps.pcps.main.model.result.SettlementSummaryResult;
import dk.apps.pcps.main.model.result.SuccessPaymentSummary;

import dk.apps.pcps.db.repository.SettlementFileUploadRepository;

import dk.apps.pcps.main.handler.ApplicationException;
import dk.apps.pcps.main.model.enums.CardEnum;
import dk.apps.pcps.main.model.enums.ProcessMessageEnum;
import dk.apps.pcps.main.model.payload.TrxHistoryData;

import dk.apps.pcps.main.model.result.fileupload.FileBatch;
import dk.apps.pcps.main.model.result.fileupload.SettlementFileUploadData;
import dk.apps.pcps.main.module.tapcash.model.CardData;
import dk.apps.pcps.main.utils.ISOConverters;
import dk.apps.pcps.main.utils.ObjectMapper;
import dk.apps.pcps.main.utils.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SettlementFileUploadImpl implements SettlementFileUploadService {

    SettlementFileUploadRepository settlementFileUploadRepository;
    TrxChannelService trxChannelService;
    FileService fileService;
    FileTransferService fileTransferService;
    DeviceUserDetailService deviceUserDetailService;
    DeviceUserAndBatchGroupService deviceUserAndBatchGroupService;
    SuccessPaymentService successPaymentService;
    TrxHistoryService trxHistoryService;
    SettlementService settlementService;

    @Autowired
    public SettlementFileUploadImpl(SettlementFileUploadRepository settlementFileUploadRepository,
                                    TrxChannelService trxChannelService, FileService fileService,
                                    FileTransferService fileTransferService,
                                    DeviceUserDetailService deviceUserDetailService, DeviceUserAndBatchGroupService deviceUserAndBatchGroupService, SuccessPaymentService successPaymentService, TrxHistoryService trxHistoryService, SettlementService settlementService){
        this.settlementFileUploadRepository = settlementFileUploadRepository;
        this.trxChannelService = trxChannelService;
        this.fileService = fileService;
        this.fileTransferService = fileTransferService;
        this.deviceUserDetailService = deviceUserDetailService;
        this.deviceUserAndBatchGroupService = deviceUserAndBatchGroupService;
        this.successPaymentService = successPaymentService;
        this.trxHistoryService = trxHistoryService;
        this.settlementService = settlementService;
    }

    @Override
    @Transactional
    public void createData(SettlementFileUpload settlementFileUpload) {
        settlementFileUploadRepository.save(settlementFileUpload);
    }

    @Override
    public void generateFile() {
        List<SettlementFileUpload> fileUploads = settlementFileUploadRepository.findAllByStatus("PENDING");
        for (SettlementFileUpload fileUpload: fileUploads){
            int batchGroupId = fileUpload.getBatchGroupId();
            String fileName = fileUpload.getFileName();
            String parentDir = new File(System.getProperty("user.dir")).getParent();
            if (parentDir == null)
                parentDir = "/opt/tomcat/";
            StringBuffer sbContent = new StringBuffer().append(fileUpload.getHeader()).append(fileUpload.getBody()).append(fileUpload.getFooter());
            String username = fileUpload.getUsername();
            CardEnum card = CardEnum.getCardEnum(batchGroupId);
            parentDir += "/file-upload/"+username+"/"+batchGroupId;
            fileService.writeTxtFile(parentDir, fileName, sbContent.toString());
        }
    }

    @Override
    public void uploadFile() {
        String baseDirName = "/file-upload";
        String parentDir = new File(System.getProperty("user.dir")).getParent();
        if (parentDir == null)
            parentDir = "/opt/tomcat/";
        parentDir += baseDirName;
        List<String> dirs = fileService.getFileAndDirs(parentDir);
        for (String userDir: dirs) {
            parentDir += "/"+userDir;
            dirs = fileService.getFileAndDirs(parentDir);
            for (String dir : dirs) {
                TrxChannel channel = trxChannelService.getTrxChannel(true, Integer.parseInt(dir));
                Sftp sftp = Utility.jsonToObject(channel.getAdditionalParam(), Sftp.class);
                //fileTransferService.connect(channel.getHost(), channel.getPort(), sftp.getUsername(), sftp.getPassword());
                String remoteDir = sftp.getPath();
                if (remoteDir == null || remoteDir.isEmpty())
                    remoteDir = "/home/diyas"+baseDirName+"/";
                parentDir += "/" + dir;
                List<String> files = fileService.getFileAndDirs(parentDir);
                for (String fileName : files) {
                    parentDir += "/" + fileName;
                    fileTransferService.createDir(remoteDir+dir+"/");
                    boolean isUploaded = fileTransferService.uploadFile(parentDir, remoteDir+dir+"/"+fileName);
                    if (isUploaded){
                        fileService.removeFile(parentDir);
                    }
                }

                parentDir = new File(System.getProperty("user.dir")).getParent() + baseDirName +"/"+userDir;
            }

        }
    }

    @Override
    public List<SettlementFileUploadData> checkResponseFile() {
        List<SettlementFileUploadData> fileUploads = new ArrayList<>();
        List<SettlementFileUpload> prepaidCards = settlementFileUploadRepository.findAllByStatusGroupByBatchGroupId("PENDING", "SUCCESS", "FAILED");
        for (SettlementFileUpload prepaidCard: prepaidCards){
            int batchGroupId = prepaidCard.getBatchGroupId();
            String prepaidCardName = CardEnum.getCardEnum(batchGroupId).name;
            String username = prepaidCard.getUsername();
            String parentDir = new File(System.getProperty("user.dir")).getParent();
            if (parentDir == null)
                parentDir = "/opt/tomcat/";

            String successDir = parentDir +"/file-upload/"+username+"/"+batchGroupId+"/success";
            String failedDir = parentDir +"/file-upload/"+username+"/"+batchGroupId+"/failed";
            List<SettlementFileUpload> batchGroups = settlementFileUploadRepository.findAllByBatchGroupId(prepaidCard.getBatchGroupId());
            String dir = null;
            int x = 0;
            List<FileBatch> batchFiles = new ArrayList<>();
            for (SettlementFileUpload batchGroup: batchGroups){
                String responseFileName = null;
                String fileName = batchGroup.getFileName().split("\\.")[0];
                List<String> files = fileService.getFileAndDirs(successDir);
                boolean isSuccess = false;
                for (String file: files){
                    responseFileName = file;
                    file = file.split("\\.")[0];
                    if (file.equals(fileName)){
                        dir = successDir;
                        isSuccess = true;
                        break;
                    }
                }

                files = fileService.getFileAndDirs(failedDir);
                boolean isFailed = false;
                for (String file: files){
                    responseFileName = file;
                    file = file.split("\\.")[0];
                    if (file.equals(fileName)){
                        dir = failedDir;
                        isFailed = true;
                        break;
                    }
                }

                int batchNo = x + 1;
                if (batchGroup.getStatus().equals("PENDING")){
                    String status = null;
                    if(isSuccess){
                        status = "SUCCESS";
                    } else if(isFailed){
                        status = "FAILED";
                    } else {
                        throw new ApplicationException(ProcessMessageEnum.FAILED);
                    }
                    batchGroup.setStatus(status);
                    batchGroup.setSettle(true);
                    settlementFileUploadRepository.save(batchGroup);
                }
                FileBatch fileBatch = new FileBatch();
                fileBatch.setBatch(batchNo);
                fileBatch.setFileName(fileName);
                fileBatch.setResponseFileName(responseFileName);
                fileBatch.setStatus(batchGroup.getStatus());
                File fileResponse = new File(dir+"/"+responseFileName);
                fileBatch.setStatus(batchGroup.getStatus());
                fileBatch.setSettleAt(new Timestamp(fileResponse.lastModified()));
                fileBatch.setStrInvoiceNums(batchGroup.getInvoiceNums());
                fileBatch.getInvoiceNums();
                batchFiles.add(fileBatch);
            }
            SettlementFileUploadData data = ObjectMapper.map(prepaidCard, SettlementFileUploadData.class);
            data.setDirectory(parentDir + dir);
            data.setPrepaidName(prepaidCardName);
            data.setBatchFiles(batchFiles);
            fileUploads.add(data);
        }
        return fileUploads;
    }

    @Override
    public List<SuccessPaymentSummary> getSettlementSummary(String username, Integer batchGroupId) {
        List<SuccessPaymentSummary> summaryList = new ArrayList<>();
        List<SettlementFileUpload> fileUploads = settlementFileUploadRepository.findAllByUsernameAndBatchGroupId(username, batchGroupId);
        for (SettlementFileUpload fileUpload: fileUploads){
            BatchGroup batchGroup = new BatchGroup();
            batchGroup.setId(fileUpload.getBatchGroupId());
            String[] arrStrINums = getInvoiceNums(fileUpload.getInvoiceNums());
            int[] arrINums = Arrays.stream(arrStrINums).mapToInt(Integer::parseInt).toArray();
            List<SuccessPayment> successPayments = successPaymentService.getSuccessPayments(username, batchGroup, arrINums);
            long totalBaseAmount = successPayments.stream().mapToLong(x -> x.getBaseAmount()).sum();
            SuccessPaymentSummary successPaymentSummary = new SuccessPaymentSummary();
            successPaymentSummary.setBatchGroupId(batchGroupId);
//            successPaymentSummary.setName(batchGroup.getName().toUpperCase());
            successPaymentSummary.setTotalAmount(totalBaseAmount);
            successPaymentSummary.setTrxCount(successPayments.size());
            successPaymentSummary.setDetailUrl("/trx/success-payment-detail/"+batchGroupId);
            successPaymentSummary.setSettlement(fileUpload.isSettle());
            successPaymentSummary.setSettlementStatus(fileUpload.getStatus());
            summaryList.add(successPaymentSummary);
        }
        return summaryList;
    }

    @Override
    public void createSettlementData() {
        List<SettlementSessionData> settlementSessionDataList = new ArrayList<>();
        List<BatchGroupData> batchGroupDataList = new ArrayList<>();
        List<SettlementFileUpload> fileUploadUsers = settlementFileUploadRepository.findAllByStatusGroupByBatchGroupId("SUCCESS");
        for (SettlementFileUpload fuUser: fileUploadUsers){
            String username = fuUser.getUsername();
            int sessNum = deviceUserDetailService.getSessionNumber(username);
            List<SettlementFileUpload> fileUploadGroups = settlementFileUploadRepository.findAllByUsernameGroupByBatchGroupId(username);
            for (SettlementFileUpload fuBatchGroup: fileUploadGroups){
                //String username = group.getUsername();
                List<SettlementFileUpload> fileUploads = settlementFileUploadRepository.findAllByBatchGroupId(fuBatchGroup.getBatchGroupId());
                for (SettlementFileUpload fileUpload: fileUploads){
                    BatchGroup batchGroup = new BatchGroup();
                    batchGroup.setId(fileUpload.getBatchGroupId());
                    String[] arrStrINums = getInvoiceNums(fuBatchGroup.getInvoiceNums());
                    int[] arrINums = Arrays.stream(arrStrINums).mapToInt(Integer::parseInt).toArray();
                    int batchNumber = deviceUserAndBatchGroupService.getBatchNumber(username, batchGroup);
                    List<SuccessPayment> successPayments = successPaymentService.getSuccessPayments(username, batchGroup, arrINums);
                    Settlement settlement = new Settlement();
                    settlement.setUsername(username);
                    settlement.setBatchGroup(batchGroup);
                    settlement.setBatchNum(batchNumber);
                    //settlement.setAdditionalParam(Utility.objectToString(settlementDetails));
                    settlement.setNumOfSuccessPayment(arrINums.length);
                    settlement.setSuccessPaymentTotalBaseAmount(fileUpload.getTotalAmount());
                    settlement.setNumOfRefund(0);
                    settlement.setRefundTotalBaseAmount(0);
                    settlement.setNumOfTips(0);
                    settlement.setTipsTotalBaseAmount(0);
                    settlement.setSettlementSessionNum(0);
                    //settlement.setSettlementHostDate(hostDate);
                    //settlement.setSettlementHostTime(hostTime);
                    //settlement.setSettlementSessionNum(sessNum);
                    //settlement.setTrxChannel(null);
                    //settlement = settlementService.createDataSettlement(settlement);
                    DeviceUserAndBatchGroup reqDeviceUserAndBatchGroup = new DeviceUserAndBatchGroup();
                    reqDeviceUserAndBatchGroup.setUsername(username);
                    reqDeviceUserAndBatchGroup.setBatchNum(batchNumber);
                    if (settlement != null) {
                        List<TrxHistoryData> trxHistoryDataList = ObjectMapper.mapAll(successPayments, TrxHistoryData.class);
                        int finalBatchNumber = batchNumber;
                        trxHistoryDataList.forEach(
                                f -> {
                                    Timestamp paidAt = Timestamp.valueOf(ISOConverters.hexToDate(getPaidAt(batchGroup.getId(), f.getAdditionalData())));
                                    f.setBatchNum(finalBatchNumber);
                                    f.setMaskedCan(Utility.maskedCan(f.getCan()));
                                    f.setPaidAt(paidAt);
                                }
                        );
                        List<TrxHistory> trxHistories = ObjectMapper.mapAll(trxHistoryDataList, TrxHistory.class);
                        //trxHistories = trxHistoryService.createTrxHistory(trxHistories);
                        if (trxHistories.size() > 0) {
                            List<Integer> invNums = Arrays.stream(arrINums).boxed().collect(Collectors.toList());
                            if (invNums.size() > 0) {
                                //successPaymentService.removeAllDataBatch(invNums);
                                //deviceUserAndBatchGroupService.updateBatchNumber(username, batchGroup, batchNumber);
                            }
                        }
                        settlementSessionDataList.add(new SettlementSessionData().setSessionNumber(sessNum).setSettlementAt(Utility.getTimeStamp()).setSettlementSummaryList(batchGroupDataList));
                        SettlementSummaryResult settlementResult = new SettlementSummaryResult().setSettlementSessionList(settlementSessionDataList);
                        log.info(Utility.objectToString(settlementResult));
                        //deviceUserDetailService.updateSessionNumber(username, sessNum);
                        //cardUpdateHistoryService.updateCardUpdateHistory(username, sessNum);
                    }
                }
            }
        }
    }

    private String getPaidAt(int batchGroupId, String addData) {
        CardEnum card = CardEnum.getCardEnum(batchGroupId);
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

    private String[] getInvoiceNums(String strInvoiceNums){
        String iNums = strInvoiceNums;
        int iNumsLength = 6;
        int start = 0;
        int end = iNumsLength;
        int iNumsCount = iNums.length() / iNumsLength;
        String[] invoiceNums = new String[iNumsCount];
        for (int i = 0; i < iNumsCount; i++) {
            invoiceNums[i] = iNums.substring(start, end);
            start = end + 1;
            end = iNumsLength + start - 1;
        }
        return invoiceNums;
    }
}
