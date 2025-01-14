package dk.apps.pcps.model.iso8583.bni.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import dk.apps.pcps.validation.constraint.HexString;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CardDataUpdate {
    @HexString
    private String can;
    @HexString
    @NotNull
    private String csn;
    @HexString
    @NotNull
    private String trn;
    @HexString
    @NotNull
    private String crn;
    @HexString
    @NotNull
    private String trp;
    @HexString
    @NotNull
    private String lastPurseStatus;
    @HexString
    @NotNull
    private String lastPurseBalance;
    @HexString
    @NotNull
    private String lastCreditTrp;
    @HexString
    @NotNull
    private String lastCreditHeader;
    @HexString
    @NotNull
    private String lastTransTrp;
    @HexString
    @NotNull
    private String lastTransRecord;
    @HexString
    @NotNull
    private String lastBdc;
    @HexString
    @NotNull
    private String lastDebitOption;
    @HexString
    @NotNull
    private String lastTransSignCert;
    @HexString
    @NotNull
    private String lastCounterData;
}
