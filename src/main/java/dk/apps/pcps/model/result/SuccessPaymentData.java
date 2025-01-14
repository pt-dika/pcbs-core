package dk.apps.pcps.model.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessPaymentData {
    private String mid;
    private String tid;
    private Long baseAmount;
    private Long currentBalance;
    private Long lastBalance;
    private String can;
    private String stan;
    private String invoiceNumber;
    private String approvalCode;
    private String rrn;
    private Boolean firstCopyFlag;
    private String printReceiptEmail;
    private Boolean printReceiptEmailFlag;
    private String printReceiptPhoneNum;
    private Boolean printReceiptPhoneNumFlag;
    private String paymentHostDate;
    private String paymentHostTime;
    private String binResultName;
    private String posRequestType;
    private String posCloudPointer;
    private String lastCounterData;
    private String lastCreditHeader;
    private String lastDebitOption;
    private String lastPurseBalance;
    private String lastPurseStatus;
    private String lastTransRecord;
    private String lastTransSignCert;
    private String lastBdc;
    private String lastCreditTrp;
    private String lastTransTrp;
    private String signCert;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+7")
    private Timestamp updateAt;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+7")
    private Timestamp createAt;
    @JsonIgnore
    private boolean refund;
}
