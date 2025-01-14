package dk.apps.pcps.main.module.emoney;

import dk.apps.pcps.config.auth.AuthService;
import dk.apps.pcps.db.entity.BatchGroup;
import dk.apps.pcps.db.entity.SettlementDataView;
import dk.apps.pcps.db.entity.SettlementFileUpload;
import dk.apps.pcps.db.service.BatchGroupService;
import dk.apps.pcps.db.service.SettlementDataViewService;
import dk.apps.pcps.db.service.SettlementFileUploadService;
import dk.apps.pcps.main.model.result.SettlementSummaryResult;
import dk.apps.pcps.main.module.emoney.model.SettlementBodyData;
import dk.apps.pcps.main.utils.ISOConverters;
import dk.apps.pcps.main.utils.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class EMoneyImpl implements EMoneyService {

    AuthService authService;
    BatchGroupService batchGroupService;
    SettlementDataViewService settlementDataViewService;
    SettlementFileUploadService settlementFileUploadService;

    @Autowired
    private EMoneyImpl(AuthService authService, BatchGroupService batchGroupService, SettlementDataViewService settlementDataViewService, SettlementFileUploadService settlementFileUploadService){
        this.authService = authService;
        this.batchGroupService = batchGroupService;
        this.settlementDataViewService = settlementDataViewService;
        this.settlementFileUploadService = settlementFileUploadService;
    }

    @Override
    public SettlementSummaryResult doSettlement() {

        return null;
    }

    @Override
    public Map generateSettlementData() {
        return generateSettlement();
    }

    private BatchGroup getBatchGroup(){
        BatchGroup batchGroup = batchGroupService.getBatchGroup(2);
        return batchGroup;
    }

    private Map generateSettlement(){
        String shiftId = "00";
        String operator = "0001";
        String datetime = Utility.getDateTime("ddMMyyyyHHmm");
        String branchCode = "";
        String sequence = "00";
        boolean isNewSettle = false;
        String username = authService.getAuthUsers().getUsername();
        StringBuffer sbFileName = new StringBuffer();
        sbFileName.append(operator);//operator
        sbFileName.append(shiftId);//shift id
        sbFileName.append(branchCode);//branch code
        sbFileName.append("");//gate
        sbFileName.append("");//substation
        if (isNewSettle)
            sbFileName.append("FF");
        sbFileName.append(sequence);//sequence
        sbFileName.append(datetime);//settle date time
        sbFileName.append("FP");//ds
        sbFileName.append(".txt");//suffix

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
            sbBody.append(bodyData.getData());
            sbBody.append("0001");
            sbBody.append(String.format("%06d", seq));
            sbBody.append("\n");
            dTotalAmount = dTotalAmount + amount;
            totalPaymentAmount += settlement.getBaseAmount();
            sbINums.append(ISOConverters.padLeftZeros(String.valueOf(settlement.getInvoiceNum()), 6));

        }

        String numOfTrx = String.format("%08d", settlements.size());
        String hTotalAmount = String.format("%012d", dTotalAmount);

        StringBuffer sbFooter = new StringBuffer();
        sbFooter.append(operator);//operator
        sbFooter.append(numOfTrx);//total record

        StringBuffer sbHeader = new StringBuffer();
        sbHeader.append("");//card type
        sbHeader.append(sbHeader.length() + sbBody.length() + sbFooter.length());//no of record
        sbHeader.append(hTotalAmount);//total amount
        sbHeader.append(shiftId);//shift id
        sbHeader.append(operator);//operator
        sbHeader.append(datetime);//date
        sbHeader.append("03");//end of char

        StringBuffer sbSettlement = new StringBuffer();
        sbSettlement.append(sbHeader);
        sbSettlement.append(sbBody);
        sbSettlement.append(sbFooter);

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
        data.setFooter(sbFooter.toString());
        data.setStatus("PENDING");
        data.setInvoiceNums(sbINums.toString());
        data.setTotalAmount(totalPaymentAmount);
        settlementFileUploadService.createData(data);

        return mSettlement;
    }
}
