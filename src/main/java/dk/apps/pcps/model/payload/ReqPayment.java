package dk.apps.pcps.model.payload;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import dk.apps.pcps.db.entity.BatchGroup;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ReqPayment<T> {
    @NotBlank
    private String invoiceNum;
    private String binResultName;
    private BatchGroup batchGroup;
    @NotBlank
    private String posRequestType;
    @NotBlank
    @Size(min = 16, max = 16)
    private String can;
    @NotNull
    @Min(value = 0)
    private Long baseAmount;
    @NotBlank
//    private String posRequestType;
//    private String posCloudPointer;
//    private boolean posCloudPushNotifFlag;
    @NotBlank
    private String username;
    //@NotNull
//    @Min(value = 0)
//    private Long balance;
//    @NotNull
//    @Min(value = 0)
//    private Long currentBalance;
//    private String tid;
//    private String mid;
    private String approvalCode;
//    @HexString
//    private String trn;
//    @HexString
//    private String crn;
    @Valid
    private T cardData;
//    @HexString
//    private String lastPurseStatus;
//    @HexString
//    private String lastPurseBalance;
//    @HexString
//    private String lastCreditTrp;
//    @HexString
//    private String lastCreditHeader;
//    @HexString
//    private String lastTransTrp;
//    @HexString
//    private String lastTransRecord;
//    @HexString
//    private String lastBdc;
//    @HexString
//    private String lastDebitOption;
//    @HexString
//    private String lastTransSignCert;
//    @HexString
//    private String lastCounterData;
//    @HexString
//    private String purseExpiry;
//    @HexString
//    private String keySet;
//    @HexString
//    private String maxCardBalance;
//    @HexString
//    private String invoiceReference;
//    @HexString
//    private String debitOption;
//    @HexString
//    private String afterBalance;
//    @HexString
//    private String trp;
//    @HexString
//    private String counterData;
//    @HexString
//    private String signCert;
}
