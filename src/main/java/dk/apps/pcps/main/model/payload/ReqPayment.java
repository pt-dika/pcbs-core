package dk.apps.pcps.main.model.payload;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
    private String userDevice;
    private String tid;
    private String mid;
    @NotNull
    private Integer batchGroupId;
    @NotBlank
    private String binResultName;
    @NotBlank
    private String invoiceNum;
    @NotBlank
    private String posRequestType;
    @NotBlank
    @Size(min = 16, max = 16)
    private String cardNumber;
    @NotNull
    @Min(value = 0)
    private Long baseAmount;
    //@NotBlank
//    private String posRequestType;
//    private String posCloudPointer;
//    private boolean posCloudPushNotifFlag;
    //@NotBlank
    private String username;
    private String approvalCode;
    @Valid
    private T cardData;

}
