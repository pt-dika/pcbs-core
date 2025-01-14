package dk.apps.pcps.main.model.result;

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
public class SuccessPaymentDetail {
    private String mid;
    private String tid;
    private Long baseAmount;
    private Long currentBalance;
    private Long lastBalance;
    private String can;
    private String stan;
    @JsonIgnore
    private Integer invoiceNum;
    private String invoiceNumber;
    private String approvalCode;
    private String rrn;
    private Boolean firstCopyFlag;
    private String printReceiptEmail;
    private Boolean printReceiptEmailFlag;
    private String printReceiptPhoneNum;
    private Boolean printReceiptPhoneNumFlag;
    private String binResultName;
    private String posRequestType;
    private String posCloudPointer;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+7")
    private Timestamp updateAt;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+7")
    private Timestamp createAt;
    @JsonIgnore
    private boolean refund;
}
