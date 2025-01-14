package dk.apps.pcps.model.payload;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ReqSettlement {
    @NotBlank
    @Size(min = 16, max = 16)
    private String can;
    private String processingCode;
    private String STAN;
    private String channel;
    private String tid;
    private String mid;
    //private AdditionalData additionalData;
    private String localDate;
    private String localTime;
    private String transmissionDateTime;
    private String merchantName;
    private String mac;
}
