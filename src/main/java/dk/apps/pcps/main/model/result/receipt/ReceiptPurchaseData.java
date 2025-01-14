package dk.apps.pcps.main.model.result.receipt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReceiptPurchaseData {
    private String tid;
    private String mid;
    private String trxType;
    private String batch;
    private String invoiceNo;
    private String date;
    private String time;
    private String ptc;
    private String approvalCode;
    private String certData;
    private String counterData;
    private String transHeader;
    private String signCert;
    private String maskedCan;
    private String prevBal;
    private String amount;
    private String currBal;

}
