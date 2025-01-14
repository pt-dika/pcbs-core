package dk.apps.pcps.main.module.flazz;

import dk.apps.pcps.config.auth.AuthService;
import dk.apps.pcps.db.entity.BatchGroup;
import dk.apps.pcps.db.entity.SettlementDataView;
import dk.apps.pcps.db.entity.SettlementFileUpload;
import dk.apps.pcps.db.service.BatchGroupService;
import dk.apps.pcps.db.service.SettlementDataViewService;
import dk.apps.pcps.db.service.SettlementFileUploadService;
import dk.apps.pcps.main.model.result.SettlementSummaryResult;
import dk.apps.pcps.main.module.flazz.model.SettlementBodyData;
import dk.apps.pcps.main.utils.ISOConverters;
import dk.apps.pcps.main.utils.Utility;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlazzImpl implements FlazzService {

    AuthService authService;
    BatchGroupService batchGroupService;
    SettlementDataViewService settlementDataViewService;
    SettlementFileUploadService settlementFileUploadService;

    @Autowired
    private FlazzImpl(AuthService authService, BatchGroupService batchGroupService, SettlementDataViewService settlementDataViewService, SettlementFileUploadService settlementFileUploadService){
        this.authService = authService;
        this.batchGroupService = batchGroupService;
        this.settlementDataViewService = settlementDataViewService;
        this.settlementFileUploadService = settlementFileUploadService;
    }

    private BatchGroup getBatchGroup(){
        BatchGroup batchGroup = batchGroupService.getBatchGroup(2);
        return batchGroup;
    }

    @Override
    public SettlementSummaryResult doSettlement() {
        return null;
    }

    @Override
    public Map generateSettlementData() {
        return null;
    }

    private Map generateSettlement(){
        String terminalId = "00";
        String batchNo = "0001";
        String date = Utility.getDateTime("yyyyMMdd");
        String year = Utility.getDateTime("yyyy");
        String time = Utility.getDateTime("hhmmss");
        String branchCode = "";
        String sequence = "00";
        boolean isNewSettle = false;
        String username = authService.getAuthUsers().getUsername();
        StringBuffer sbFileName = new StringBuffer();
        sbFileName.append(date);//date
        sbFileName.append(terminalId);//terminal id
        sbFileName.append(batchNo);//batch no
        sbFileName.append(time);//time
        sbFileName.append(".txn");//suffix

        List<SettlementDataView> settlements = settlementDataViewService.getDataForSettlement(getBatchGroup(), username);
        StringBuffer sbBody = new StringBuffer();
        int seq = 0;
        long dTotalAmount = 0;
        long totalPaymentAmount = 0;
        StringBuffer sbINums = new StringBuffer();
        for (SettlementDataView settlement: settlements){
            seq = seq + 1;
            SettlementBodyData bodyData = Utility.jsonToObject(settlement.getAdditionalData(), SettlementBodyData.class);
            byte[] baAmount = ISOConverters.hexStringToBytes(bodyData.getAmount());
            long amount = ISOConverters.convertBalanceEMoney(baAmount);
            String hexAmount = ISOConverters.bytesToHex(ISOConverters.padLeftZeros(baAmount, 8));
            bodyData.setAmount(hexAmount);
            byte[] baLastBalance = ISOConverters.hexStringToBytes(bodyData.getLastBalance());
            String hexLastBalance= ISOConverters.bytesToHex(ISOConverters.padLeftZeros(baLastBalance, 8));
            bodyData.setLastBalance(hexLastBalance);
            sbBody.append("0220");//record type
            sbBody.append(bodyData.getData());
            dTotalAmount = dTotalAmount + amount;
            totalPaymentAmount += settlement.getBaseAmount();
            sbINums.append(ISOConverters.padLeftZeros(String.valueOf(settlement.getInvoiceNum()), 6));

        }

        String numOfTrx = String.format("%08d", settlements.size());
        String hTotalAmount = String.format("%012d", dTotalAmount);

        StringBuffer sbHeader = new StringBuffer();
        sbHeader.append("BULKFILE");//file id
        sbHeader.append("02");//version

        StringBuffer sbFooter = new StringBuffer();
        sbFooter.append("0500");//record type
        sbFooter.append(terminalId);//terminal id
        sbFooter.append("");//merchant id
        sbFooter.append(batchNo);//batch no
        sbFooter.append(numOfTrx);//sales count
        sbFooter.append(hTotalAmount);//sales amount
        sbFooter.append(time);//settle time
        sbFooter.append(date);//settle date
        sbFooter.append(year);//settle year
        sbFooter.append("");//reserved

        StringBuffer sbSettlement = new StringBuffer();
        sbSettlement.append(sbHeader);
        sbSettlement.append(sbBody);

        Map mSettlement = new HashMap();
        mSettlement.put("file_name", sbFileName);
        mSettlement.put("content", sbSettlement);
        mSettlement.put("header", sbHeader);
        mSettlement.put("body", sbBody);
        mSettlement.put("footer", sbFooter);

        SettlementFileUpload data = new SettlementFileUpload();
        data.setBatchGroupId(2);
        data.setUsername(username);
        data.setFileName(sbFileName.toString());
        data.setHeader(sbHeader.toString());
        data.setBody(sbBody.toString());
        data.setStatus("PENDING");
        data.setInvoiceNums(sbINums.toString());
        data.setTotalAmount(totalPaymentAmount);
        settlementFileUploadService.createData(data);

        return mSettlement;
    }
}
