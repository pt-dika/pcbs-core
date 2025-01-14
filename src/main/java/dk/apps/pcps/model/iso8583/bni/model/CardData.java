package dk.apps.pcps.model.iso8583.bni.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import dk.apps.pcps.commonutils.Utility;
import dk.apps.pcps.validation.constraint.HexString;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CardData {
    @HexString
    private String transType;
    @HexString
    private String txnDateTime;
    @HexString
    private String debitOption;
    @HexString
    private String debitAmount;
    //TID
    @HexString
    private String trp;
    @HexString
    private String trn;
    @HexString
    private String crn;
    //SRP
    @HexString
    private String purseStatus;
    @HexString
    private String purseBalance;
    @HexString
    private String purseExpiry;
    @HexString
    private String lastCreditTrp;
    @HexString
    private String lastCreditHeader;
    @HexString
    private String lastTxnTrp;
    @HexString
    private String lastTxnRecord;
    @HexString
    private String bdc;
    @HexString
    private String keySet;
    @HexString
    private String maxCardBalance;
    //VSRP
    @HexString
    private String lastTransSignCert;
    @HexString
    private String lastCounterData;
    //VDR
    @HexString
    private String lastPurseBalance;
    @HexString
    private String signCert;
    @HexString
    private String counterData;

    public String getTransHeader(){
        return Utility.hexString(
                this.getTransType(),
                this.getDebitAmount(),
                this.getTxnDateTime()
        );
    }

    public String getTransData(){
        return Utility.hexString(
                this.getDebitOption(),
                this.getLastPurseBalance(),
                this.getTrp(),
                this.getCounterData(),
                this.getSignCert(),
                this.getDebitAmount()
        );
    }
}
