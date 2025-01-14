package dk.apps.pcps.main.module.tapcash.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import dk.apps.pcps.commonutils.ISOConverters;
import dk.apps.pcps.commonutils.Utility;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FileData {
    //Upload File
    private String can;
    private String transHeader;
    private String transType;
    private String transData;
    private String lastPurseStatus;
    private String lastPurseBalance;
    private String lastCreditTRP;
    private String lastCreditHeader;
    private String lastTransTRP;
    private String lastTransRecord;
    private String lastBDC;
    private String lastDebitOption;
    private String lastTransSignCert;
    private String lastCounterData;
    private String invoiceReference;

    //Parameter Download
    private String statementFee;
    private String gracePeriodFee;
    private String gracePeriod;
    private String autoSettleTime;
    private String autoLogonTime;
    private String allowedTrans;
    private String rfu;

    //Blacklist Download
    private String count;
    private String function;
    private String bdc;
    private String bdcCheckIndicator;


    public String reqFileDataSettlement(List<FileData> lst){
        String[] strArr = new String[lst.size()];
        int i = 0;
        for (FileData fileData: lst){
            String data = Utility.hexString(
                    fileData.getCan(),
                    fileData.getTransType(),
                    fileData.getTransHeader(),
                    fileData.getTransData(),
                    fileData.getLastPurseStatus(),
                    fileData.getLastPurseBalance(),
                    fileData.getLastCreditTRP(),
                    fileData.getLastCreditHeader(),
                    fileData.getLastTransTRP(),
                    fileData.getLastTransRecord(),
                    fileData.getLastBDC(),
                    fileData.getLastDebitOption(),
                    fileData.getLastTransSignCert(),
                    fileData.getLastCounterData(),
                    fileData.getInvoiceReference()
            );
            strArr[i] = data;
            i++;
        }
        return Utility.hexString(strArr);
    }

    public FileData respFileDataSettlement(String fileData){
        byte[] bData = Utility.hexStringToByteArray(fileData);
        byte[] bCan = Arrays.copyOfRange(bData, 0, 8);
        byte[] bTransType = Arrays.copyOfRange(bData, 8, 9);
        byte[] bTransHeader = Arrays.copyOfRange(bData, 9, 17);
        byte[] bTransData = Arrays.copyOfRange(bData, 17, 54);
        byte[] bLastPurseStatus = Arrays.copyOfRange(bData, 54, 55);
        byte[] bLastPurseBalance = Arrays.copyOfRange(bData, 55, 58);
        byte[] bLastCreditTRP = Arrays.copyOfRange(bData, 58, 62);
        byte[] bLastCreditHeader = Arrays.copyOfRange(bData, 62, 70);
        byte[] bLastTransTRP = Arrays.copyOfRange(bData, 70, 74);
        byte[] bLastTransRecord = Arrays.copyOfRange(bData, 74, 90);
        byte[] bLastBDC = Arrays.copyOfRange(bData, 90, 91);
        byte[] bLastDebitOption = Arrays.copyOfRange(bData, 91,92);
        byte[] bLastTransSignCert = Arrays.copyOfRange(bData, 92, 100);
        byte[] bLastCounterData = Arrays.copyOfRange(bData, 100, 108);
        byte[] bInvoiceReference = Arrays.copyOfRange(bData, 108, bData.length);
        this.setCan(ISOConverters.bytesToHex(bCan));
        this.setTransType(ISOConverters.bytesToHex(bTransType));
        this.setTransHeader(ISOConverters.bytesToHex(bTransHeader));
        this.setTransData(ISOConverters.bytesToHex(bTransData));
        this.setLastPurseStatus(ISOConverters.bytesToHex(bLastPurseStatus));
        this.setLastPurseBalance(ISOConverters.bytesToHex(bLastPurseBalance));
        this.setLastCreditTRP(ISOConverters.bytesToHex(bLastCreditTRP));
        this.setLastCreditHeader(ISOConverters.bytesToHex(bLastCreditHeader));
        this.setLastTransTRP(ISOConverters.bytesToHex(bLastTransTRP));
        this.setLastTransRecord(ISOConverters.bytesToHex(bLastTransRecord));
        this.setLastBDC(ISOConverters.bytesToHex(bLastBDC));
        this.setLastDebitOption(ISOConverters.bytesToHex(bLastDebitOption));
        this.setLastTransSignCert(ISOConverters.bytesToHex(bLastTransSignCert));
        this.setLastCounterData(ISOConverters.bytesToHex(bLastCounterData));
        this.setInvoiceReference(ISOConverters.bytesToHex(bInvoiceReference));
        return this;
    }

    public String reqFileDataBlacklist(){
        return Utility.hexString(
                //Blacklist Download
                this.getCount(),
                this.getCan(),
                this.getFunction(),
                this.getBdc(),
                this.getBdcCheckIndicator()
        );
    }

    public FileData respFileDataBalcklist(String fileData){
        byte[] bData = ISOConverters.hexStringToBytes(fileData);
        byte[] bCount = Arrays.copyOfRange(bData, 0, 2);
        byte[] bCan = Arrays.copyOfRange(bData, 2, 10);
        byte[] bFunction = Arrays.copyOfRange(bData, 10, 11);
        byte[] bBdc = Arrays.copyOfRange(bData, 11, 12);
        byte[] bBdcCheckIndicator = Arrays.copyOfRange(bData, 12, bData.length);
        this.setCount(ISOConverters.bytesToHex(bCount));
        this.setCan(ISOConverters.bytesToHex(bCan));
        this.setFunction(ISOConverters.bytesToHex(bFunction));
        this.setBdc(ISOConverters.bytesToHex(bBdc));
        this.setBdcCheckIndicator(ISOConverters.bytesToHex(bBdcCheckIndicator));
        this.setCount(ISOConverters.bytesToHex(bCount));
        return this;
    }

    public String reqFileDataParameter(){
        return Utility.hexString(
                //Parameter Download
                this.getStatementFee(),
                this.getGracePeriodFee(),
                this.getGracePeriod(),
                this.getAutoSettleTime(),
                this.getAutoLogonTime(),
                this.getAllowedTrans(),
                this.getRfu()
        );
    }

    public FileData respFileDataParameter(String fileData){
        byte[] bData = ISOConverters.hexStringToBytes(fileData);
        byte[] bStatementFee = Arrays.copyOfRange(bData, 0, 2);
        byte[] bGracePeriodFee = Arrays.copyOfRange(bData, 2, 4);
        byte[] bGracePeriod = Arrays.copyOfRange(bData, 4, 6);
        byte[] bAutoSettleTime = Arrays.copyOfRange(bData, 6, 8);
        byte[] bAutoLogonTime = Arrays.copyOfRange(bData, 8, 10);
        //byte[] bAllowedTrans = Arrays.copyOfRange(bData, 10, 26);
        byte[] bRFU = Arrays.copyOfRange(bData, 10, bData.length);
        this.setStatementFee(ISOConverters.bytesToHex(bStatementFee));
        this.setGracePeriodFee(ISOConverters.bytesToHex(bGracePeriodFee));
        this.setGracePeriod(ISOConverters.bytesToHex(bGracePeriod));
        this.setAutoSettleTime(ISOConverters.bytesToHex(bAutoSettleTime));
        this.setAutoLogonTime(ISOConverters.bytesToHex(bAutoLogonTime));
        //this.setAllowedTrans(ISOConverters.bytesToHex(bAllowedTrans));
        this.setRfu(ISOConverters.bytesToHex(bRFU));
        return this;
    }



}
