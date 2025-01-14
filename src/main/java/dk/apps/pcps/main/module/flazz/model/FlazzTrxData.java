package dk.apps.pcps.main.module.flazz.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Size;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FlazzTrxData {
    @Size(min = 2, max = 2)
    private String recordType;
    @Size(min = 6, max = 6)
    private String cardNo;
    @Size(min = 16, max = 16)
    private String expiryDate;
    @Size(min = 12, max = 12)
    private String amount;
    @Size(min = 6, max = 6)
    private String traceNo;
    @Size(min = 6, max = 6)
    private String trxDate;
    @Size(min = 4, max = 4)
    private String trxTime;
    @Size(min = 3, max = 3)
    private String posEntryMode;
    @Size(min = 3, max = 3)
    private String nii;
    @Size(min = 2, max = 2)
    private String posConditionCode;
    @Size(min = 8, max = 8)
    private String terminalId;
    @Size(min = 15, max = 15)
    private String merchantId;
    @Size(min = 6, max = 6)
    private String invoiceNo;
    @Size(min = 1, max = 1)
    private String updateStatus;
    private PaymentInfo paymentInfo;
    @Size(min = 16, max = 16)
    private String lastBalance;
    @Size(min = 16, max = 16)
    private String currentBalance;
}
