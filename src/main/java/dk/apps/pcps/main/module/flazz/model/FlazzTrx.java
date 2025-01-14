package dk.apps.pcps.main.module.flazz.model;

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
public class FlazzTrx {
    @NotBlank
    @Size(min = 16, max = 16)
    private String cardNumber;
    @NotBlank
    @Size(min = 6, max = 6)
    private String cardExpiredDate;
    @NotBlank
    @Size(min = 14, max = 14)
    private String trxDateTime;
    @NotBlank
    @Size(min = 10, max = 10)
    private String updateCardBalance;
    @NotBlank
    @Size(min = 10, max = 10)
    private String baseAmount;
    @NotBlank
    @Size(min = 1, max = 1)
    private String completionCode;
    @NotBlank
    @Size(min = 8, max = 8)
    private String samId;
    @NotBlank
    @Size(min = 8, max = 8)
    private String samTransNo;
    @NotBlank
    @Size(min = 16, max = 16)
    private String samRandomNo;
    @NotBlank
    @Size(min = 16, max = 16)
    private String samCryptogram;
    @NotBlank
    @Size(min = 8, max = 8)
    private String cardCryptogram;
    @NotBlank
    @Size(min = 6, max = 6)
    private String cardTransNo;
    @NotBlank
    @Size(min = 4, max = 4)
    private String cardDebitCertificate;
    @NotBlank
    @Size(min = 12, max = 12)
    private String mid;
    @NotBlank
    @Size(min = 8, max = 8)
    private String tid;
    @NotBlank
    @Size(min = 16, max = 16)
    private String trn;
    @NotBlank
    @Size(min = 2, max = 2)
    private String version;
    @NotBlank
    @Size(min = 4, max = 4)
    private String track2ExpiredDate;
    @NotBlank
    @Size(min = 20, max = 20)
    private String reserved;
    @NotBlank
    @HexString
    @Size(min = 6, max = 6)
    private String currentBalance;
    @NotBlank
    @HexString
    @Size(min = 6, max = 6)
    private String lastBalance;

}
