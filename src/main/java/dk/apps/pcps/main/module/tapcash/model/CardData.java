package dk.apps.pcps.main.module.tapcash.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import dk.apps.pcps.commonutils.Utility;
import dk.apps.pcps.validation.constraint.HexString;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CardData {
    @NotBlank
    @HexString
    @Size(min = 16, max = 16)
    private String can;
    @NotBlank
    @HexString
    @Size(min = 16, max = 16)
    private String samId;
    @NotBlank
    @HexString
    @Size(min = 30, max = 30)
    private String invoiceReference;
    @NotBlank
    @HexString
    @Size(min = 2, max = 2)
    private String transType;
    @NotBlank
    @HexString
    @Size(min = 8, max = 8)
    private String txnDateTime;
    @NotBlank
    @HexString
    @Size(min = 2, max = 2)
    private String debitOption;
    @NotBlank
    @HexString
    @Size(min = 6, max = 6)
    private String debitAmount;
    //TID
    @NotBlank
    @HexString
    @Size(min = 8, max = 8)
    private String trp;
    @NotBlank
    @HexString
    @Size(min = 16, max = 16)
    private String trn;
    @NotBlank
    @HexString
    @Size(min = 16, max = 16)
    private String crn;
    //SRP
    @NotBlank
    @HexString
    @Size(min = 2, max = 2)
    private String purseStatus;
    @NotBlank
    @HexString
    @Size(min = 6, max = 6)
    private String purseBalance;
    @NotBlank
    @HexString
    @Size(min = 4, max = 4)
    private String purseExpiry;
    @NotBlank
    @HexString
    @Size(min = 8, max = 8)
    private String lastCreditTrp;
    @NotBlank
    @HexString
    @Size(min = 16, max = 16)
    private String lastCreditHeader;
    @NotBlank
    @HexString
    @Size(min = 8, max = 8)
    private String lastTxnTrp;
    @NotBlank
    @HexString
    @Size(min = 32, max = 32)
    private String lastTxnRecord;
    @NotBlank
    @HexString
    @Size(min = 2, max = 2)
    private String bdc;
    @NotBlank
    @HexString
    @Size(min = 2, max = 2)
    private String keySet;
    @NotBlank
    @HexString
    @Size(min = 6, max = 6)
    private String maxCardBalance;
    //VSRP
    @NotBlank
    @HexString
    @Size(min = 16, max = 16)
    private String lastTransSignCert;
    @NotBlank
    @HexString
    @Size(min = 16, max = 16)
    private String lastCounterData;
    //VDR
    @NotBlank
    @HexString
    @Size(min = 6, max = 6)
    private String lastPurseBalance;
    @NotBlank
    @HexString
    @Size(min = 16, max = 16)
    private String signCert;
    @NotBlank
    @HexString
    @Size(min = 16, max = 16)
    private String counterData;

    public String getTransHeader(){
        return Utility.hexString(
                this.getTransType(),
                this.getDebitAmount(),
                this.getTxnDateTime()
        );
    }

    public String getStringData(){
        StringBuffer sb = new StringBuffer();
        sb.append(this.getTransType());
        sb.append(this.getBdc());
        sb.append(this.getCan());
        sb.append(this.getTransHeader());
        sb.append(this.getTrp());
        sb.append(this.getDebitOption());
        sb.append(this.getPurseBalance());
        sb.append(this.getLastPurseBalance());
        sb.append(this.getCounterData());
        sb.append(this.getSignCert());
        sb.append(this.getLastCreditTrp());
        sb.append(this.getLastCreditHeader());
        sb.append(this.getLastTxnTrp());
        sb.append(this.getLastTxnRecord());
        sb.append(this.getLastTransSignCert());
        sb.append(this.getLastCounterData());
        sb.append(this.getSamId());
        sb.append("000000000000000000000000000000");
        return sb.toString();
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
