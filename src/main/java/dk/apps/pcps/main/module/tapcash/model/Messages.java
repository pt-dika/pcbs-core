package dk.apps.pcps.main.module.tapcash.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import dk.apps.pcps.db.entity.TrxChannel;
import dk.apps.pcps.model.payload.MerchantAndUser;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Messages extends MerchantAndUser {
    private TrxChannel trxChannel;
    private String MTI;
    private String responseCode;
    private String RRN;
    private String PAN;
    private String CAN;
    private String transAmount;
    private String dateSettlement;
    private String retrievalRefNumber;
    private String transFee;
    private String transCurrencyCode;
    private String processingCode;
    private String STAN;
    private String channel;
    private String TID;
    private String MID;
    private AdditionalData additionalData;
    private String localDate;
    private String localTime;
    private String transmissionDateTime;
    private String merchantName;
    private String MAC;
    private String MACK;
}
