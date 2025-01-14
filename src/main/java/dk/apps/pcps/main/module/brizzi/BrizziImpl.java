package dk.apps.pcps.main.module.brizzi;

import dk.apps.pcps.config.auth.AuthService;
import dk.apps.pcps.db.entity.BatchGroup;
import dk.apps.pcps.db.entity.SettlementDataView;
import dk.apps.pcps.db.entity.SettlementFileUpload;
import dk.apps.pcps.db.service.BatchGroupService;
import dk.apps.pcps.db.service.SettlementDataViewService;
import dk.apps.pcps.db.service.SettlementFileUploadService;
import dk.apps.pcps.main.model.result.SettlementSummaryResult;
import dk.apps.pcps.main.module.brizzi.model.BrizziSettlementBodyData;
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
public class BrizziImpl implements BrizziService {

    AuthService authService;
    BatchGroupService batchGroupService;
    SettlementDataViewService settlementDataViewService;
    SettlementFileUploadService settlementFileUploadService;

    @Autowired
    private BrizziImpl(AuthService authService, BatchGroupService batchGroupService, SettlementDataViewService settlementDataViewService, SettlementFileUploadService settlementFileUploadService){
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
        String coopCode = "808112";
        String mid = "000001111122222";
        String tid = "12345678";
        String datetime = Utility.getDateTime("ddMMyyyyhhmmss");
        String username = authService.getAuthUsers().getUsername();
        StringBuffer sbFileName = new StringBuffer();
        sbFileName.append(coopCode);//operator
        sbFileName.append(mid);//shift id
        sbFileName.append(datetime);//branch code
        sbFileName.append(".bri");//suffix

        List<SettlementDataView> settlements = settlementDataViewService.getDataForSettlement(getBatchGroup(), username);
        StringBuffer sbBody = new StringBuffer();
        int seq = 0;
        long dTotalAmount = 0;
        long totalPaymentAmount = 0;
        StringBuffer sbINums = new StringBuffer();
        for (SettlementDataView settlement: settlements){
            seq = seq + 1;
            BrizziSettlementBodyData bodyData = Utility.jsonToObject(settlement.getAdditionalData(), BrizziSettlementBodyData.class);
            bodyData.setMid(mid);
            bodyData.setTid(tid);
            byte[] baAmount = ISOConverters.hexStringToBytes(bodyData.getAmount());
            long amount = ISOConverters.convertAmountBrizzi(baAmount);
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

        StringBuffer sbHeader = new StringBuffer();
        sbHeader.append(numOfTrx);//count
        sbHeader.append(hTotalAmount);//sum

        StringBuffer sbSettlement = new StringBuffer();
        sbSettlement.append(sbHeader);
        sbSettlement.append(sbBody);

        Map mSettlement = new HashMap();
        mSettlement.put("file_name", sbFileName);
        mSettlement.put("content", sbSettlement);
        mSettlement.put("header", sbHeader);
        mSettlement.put("body", sbBody);

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
