package dk.apps.pcps.model.iso8583.bni.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import dk.apps.pcps.commonutils.ISOConverters;
import dk.apps.pcps.commonutils.Utility;
import lombok.Data;

import java.util.*;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AdditionalData {
    private Integer[] invoiceNumbers;
    //Init
    private String seed;
    private String tmk;
    private String mtmkIndex;
    private String currentBlacklistVersion;
    //Init Response
    private String year;
    private String rn;
    private String kek1;
    private String mack;
    private String kek2;
    private String ppk;
    private String debitKeyNo;
    private String blacklistVersionFrom;
    private String blacklistVersionTo;
    private String blacklistFileAction;
    //settlement
    private String batchNo;
    private String batchSeqNo;
    private String endOfBatch;
    private String numberOfTrans;
    private List<FileData> fileData;
    //parameter download
    private String softwareVersion;
    private String fileName;
    private String parameterVersion;
    private String recordNumber;
    //blacklist download
    private String blacklistVersion;
    private String nextRecordNumber;
    //un-marry sam
    private String samId;
    private String crn;
    private String rrn;
    //un-marry sam response
    private String unMarryCode;
    private String externalAuthCode;
    //topup request
    private String CAN;
    private String CSN;
    private String TRP;
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
    private String transCurrencyCode;
    //topup response
    private String transLogRecord;
    private String creditCryptogram;

    private String TRN;

    private int additionalDataSize;

    public String reqAdditionalDataInit(){
        String data = Utility.hexString(
                this.getSoftwareVersion(),
                this.getSeed(),
                this.getMtmkIndex(),
                this.getCurrentBlacklistVersion());
        this.setAdditionalDataSize(data.length());
        return data;
    }

    public AdditionalData respAdditionalDataInit(String additionalData){
        if (additionalData == null || additionalData.equalsIgnoreCase("0000"))
            return null;
        byte[] bData = Utility.hexStringToByteArray(additionalData);
        byte[] bYear = Arrays.copyOfRange(bData, 0, 2);
        byte[] brN = Arrays.copyOfRange(bData, 2, 18);
        byte[] bKek1 = Arrays.copyOfRange(bData, 18, 34);
        byte[] bMack = Arrays.copyOfRange(bData, 34, 50);
        byte[] bKek2 = Arrays.copyOfRange(bData, 50, 66);
        byte[] bPpk = Arrays.copyOfRange(bData, 66, 82);
        byte[] bDebitKeyNo = Arrays.copyOfRange(bData, 82, 83);
        byte[] bBlacklistVersionFrom = Arrays.copyOfRange(bData, 83, 86);
        byte[] bBlacklistVersionTo = Arrays.copyOfRange(bData, 86, 89);
        byte[] bBlacklistFileAction = Arrays.copyOfRange(bData, 89, 90);
        byte[] bParameterVersion = Arrays.copyOfRange(bData, 90, bData.length);
//        bKek1 = TripleDESFunction.decrypt3DES(bKek1, brN);
//        System.out.println("KEK1-Decripted: "+Utility.bytesToHex(bKek1));
//        bMack = TripleDESFunction.decrypt3DES(bMack, bKek1);
//        System.out.println("MACK-Decripted: "+Utility.bytesToHex(bMack));
        this.setYear(Utility.bytesToHex(bYear));
        this.setRn(Utility.bytesToHex(brN));
        this.setKek1(Utility.bytesToHex(bKek1));
        this.setMack(Utility.bytesToHex(bMack));
        this.setKek2(Utility.bytesToHex(bKek2));
        this.setPpk(Utility.bytesToHex(bPpk));
        this.setDebitKeyNo(Utility.bytesToHex(bDebitKeyNo));
        this.setBlacklistVersionFrom(Utility.bytesToHex(bBlacklistVersionFrom));
        this.setBlacklistVersionTo(Utility.bytesToHex(bBlacklistVersionTo));
        this.setBlacklistFileAction(Utility.bytesToHex(bBlacklistFileAction));
        this.setParameterVersion(Utility.bytesToHex(bParameterVersion));
        this.setAdditionalDataSize(additionalData.length());
        return this;
    }

    public String reqAdditionalDataLogon(){
        return Utility.hexString(this.getSoftwareVersion());
    }

    public String reqAdditionalDataLogoff(){
        return Utility.hexString(this.getSoftwareVersion());
    }

    public Map reqAdditionalDataSettlement(){
        Map map = new HashMap();
        map.put("invoice_numbers", this.getInvoiceNumbers());
        map.put("req_settlement", Utility.hexString(
                this.getSoftwareVersion(),
                this.getBatchNo(),
                this.getBatchSeqNo(),
                this.getEndOfBatch(),
                this.getNumberOfTrans(),
                this.getFileData().get(0).reqFileDataSettlement(this.getFileData())
        ));
        return map;
    }

    public AdditionalData respAdditionalDataSettlement(String additionalData){
        if (additionalData == null || additionalData.equalsIgnoreCase("0000"))
            return null;
        byte[] bData = ISOConverters.hexStringToBytes(additionalData);
        byte[] bNextRecordNumber = Arrays.copyOfRange(bData, 0, 2);
        byte[] bFileData = Arrays.copyOfRange(bData, 2, bData.length);
        List<FileData> fileDatas = new ArrayList<>();
        fileDatas.add(new FileData().respFileDataParameter(Utility.bytesToHex(bFileData)));
        this.setNextRecordNumber(Utility.bytesToHex(bNextRecordNumber));
        this.setFileData(fileDatas);
        this.setAdditionalDataSize(additionalData.length());
        return this;
    }


    public String reqAdditionalDataDownloadParameter(){
        return Utility.hexString(
                this.getSoftwareVersion(),
                ISOConverters.asciiToHex(this.getFileName()),
                this.getParameterVersion(),
                this.getRecordNumber()

        );
    }


    public AdditionalData respAdditionalDataDownloadParameter(String additionalData){
        if (additionalData == null || additionalData.equalsIgnoreCase("0000"))
            return null;
        byte[] bData = ISOConverters.hexStringToBytes(additionalData);
        byte[] bNextRecordNumber = Arrays.copyOfRange(bData, 0, 2);
        byte[] bFileData = Arrays.copyOfRange(bData, 2, bData.length);
        List<FileData> fileDatas = new ArrayList<>();
        fileDatas.add(new FileData().respFileDataParameter(Utility.bytesToHex(bFileData)));
        this.setNextRecordNumber(Utility.bytesToHex(bNextRecordNumber));
        this.setFileData(fileDatas);
        this.setAdditionalDataSize(additionalData.length());
        return this;
    }

    public String reqAdditionalDataDownloadBlacklist(){
        return Utility.hexString(
                this.getSoftwareVersion(),
                ISOConverters.asciiToHex(this.getFileName()),
                this.getBlacklistVersion(),
                this.getRecordNumber()

        );
    }

    public AdditionalData respAdditionalDataDownloadBlacklist(String additionalData){
        if (additionalData == null || additionalData.equalsIgnoreCase("0000"))
            return null;
        byte[] bData = ISOConverters.hexStringToBytes(additionalData);
        byte[] bNextRecordNumber = Arrays.copyOfRange(bData, 0, 2);
        int start = Integer.parseInt(this.recordNumber);
        int next = Integer.parseInt(ISOConverters.bytesToHex(bNextRecordNumber));
        int count = next - start;
        byte[] bFileData = Arrays.copyOfRange(bData, 2, bData.length);
        List<FileData> fileDatas = new ArrayList<>();
        int split = 13;
        for (int i = 0; i < count; i++) {
            int x = i + 1;
            try{
                byte[] file = Arrays.copyOfRange(bFileData, i * split, split * x);
                fileDatas.add(new FileData().respFileDataBalcklist(ISOConverters.bytesToHex(file)));
            } catch (IndexOutOfBoundsException e){
                break;
            }
        }
        this.setNextRecordNumber(Utility.bytesToHex(bNextRecordNumber));
        this.setFileData(fileDatas);
        this.setAdditionalDataSize(additionalData.length());
        return this;
    }

    public String reqAdditionalDataUnMarrySam(){
        return Utility.hexString(
                this.getSamId(),
                this.getCrn(),
                this.getRrn()
        );
    }

    public AdditionalData respAdditionalDataUnMarrySam(String additionalData){
        if (additionalData == null || additionalData.equalsIgnoreCase("0000"))
            return null;
        byte[] bData = Utility.hexStringToByteArray(additionalData);
        byte[] bUnMarryCode = Arrays.copyOfRange(bData, 0, 8);
        byte[] bExternalAuthCode = Arrays.copyOfRange(bData, 8, bData.length);
        this.setUnMarryCode(Utility.bytesToHex(bUnMarryCode));
        this.setExternalAuthCode(Utility.bytesToHex(bExternalAuthCode));
        this.setAdditionalDataSize(additionalData.length());
        return this;
    }

    public String reqAdditionalDataCardUpdate(){
        return Utility.hexString(
                this.getSoftwareVersion(),
                this.getCAN(),
                this.getCSN(),
                this.getTRN(),
                this.getCrn(),
                this.getTRP(),
                this.getLastPurseStatus(),
                this.getLastPurseBalance(),
                this.getLastCreditTRP(),
                this.getLastCreditHeader(),
                this.getLastTransTRP(),
                this.getLastTransRecord(),
                this.getLastBDC(),
                this.getLastDebitOption(),
                this.getLastTransSignCert(),
                this.getLastCounterData());
    }

    public AdditionalData respAdditionalDataCardUpdate(String additionalData){
        if (additionalData == null || additionalData.equalsIgnoreCase("0000"))
            return null;
        byte[] bData = Utility.hexStringToByteArray(additionalData);
        byte[] bTransLogRecord = Arrays.copyOfRange(bData, 0, 16);
        byte[] bCreditCryptogram = Arrays.copyOfRange(bData, 16, bData.length);
        this.setTransLogRecord(Utility.bytesToHex(bTransLogRecord));
        this.setCreditCryptogram(Utility.bytesToHex(bCreditCryptogram));
        this.setAdditionalDataSize(additionalData.length());
        return this;
    }

    public String reqAdditionalDataTopup(){
        return Utility.hexString(
                this.getSoftwareVersion(),
                this.getCAN(),
                this.getCSN(),
                this.getRrn(),
                this.getCrn(),
                this.getTRP(),
                this.getLastPurseStatus(),
                this.getLastPurseBalance(),
                this.getLastCreditTRP(),
                this.getLastCreditHeader(),
                this.getLastTransTRP(),
                this.getLastTransRecord(),
                this.getLastBDC(),
                this.getLastDebitOption(),
                this.getLastTransSignCert(),
                this.getLastCounterData(),
                this.getTransCurrencyCode()
        );
    }

    public AdditionalData respAdditionalDataTopup(String additionalData){
        if (additionalData == null || additionalData.equalsIgnoreCase("0000"))
            return null;
        byte[] bData = Utility.StringToByteArray(additionalData);
        byte[] bTransLogRecord = Arrays.copyOfRange(bData, 0, 16);
        byte[] bCreditCryptogram = Arrays.copyOfRange(bData, 16, bData.length);
        this.setTransLogRecord(Utility.bytesToHex(bTransLogRecord));
        this.setCreditCryptogram(Utility.bytesToHex(bCreditCryptogram));
        this.setAdditionalDataSize(additionalData.length());
        return this;
    }
}
