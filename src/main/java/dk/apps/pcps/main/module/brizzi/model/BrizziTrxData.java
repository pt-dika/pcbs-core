package dk.apps.pcps.main.module.brizzi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import dk.apps.pcps.validation.constraint.HexString;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BrizziTrxData {
    @NotBlank
    @HexString
    @Size(min = 16, max = 16)
    private String cardNo;
    @NotBlank
    @HexString
    @Size(min = 6, max = 6)
    private String amount;
    @NotBlank
    @HexString
    @Size(min = 6, max = 6)
    private String currentBalance;
    @NotBlank
    @HexString
    @Size(min = 6, max = 6)
    private String lastBalance;
    @NotBlank
    @HexString
    @Size(min = 6, max = 6)
    private String trxDate;
    @NotBlank
    @HexString
    @Size(min = 6, max = 6)
    private String trxTime;
    @NotBlank
    @HexString
    @Size(min = 6, max = 6)
    private String procCode;
    @NotBlank
    @HexString
    @Size(min = 6, max = 6)
    private String refNum;
    @HexString
    @Size(min = 2, max = 2)
    private String batchNum;
    @NotBlank
    @HexString
    @Size(min = 8, max = 8)
    private String hash;
}
