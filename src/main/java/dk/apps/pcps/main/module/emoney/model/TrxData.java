package dk.apps.pcps.main.module.emoney.model;

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
public class TrxData {
    @NotBlank
    @HexString
    @Size(min = 16, max = 16)
    private String cardNo;
    @NotBlank
    @HexString
    @Size(min = 4, max = 4)
    private String instNo;
    @NotBlank
    @HexString
    @Size(min = 14, max = 14)
    private String chipSamUid;
    @NotBlank
    @HexString
    @Size(min = 8, max = 8)
    private String terminalId;
    @NotBlank
    @HexString
    @Size(min = 4, max = 4)
    private String trxCode;
    @NotBlank
    @HexString
    @Size(min = 8, max = 8)
    private String amount;
    @NotBlank
    @HexString
    @Size(min = 8, max = 8)
    private String currentBalance;
    @NotBlank
    @HexString
    @Size(min = 8, max = 8)
    private String lastBalance;
    @NotBlank
    @HexString
    @Size(min = 12, max = 12)
    private String trxDateTime;
    @NotBlank
    @HexString
    @Size(min = 8, max = 8)
    private String mainCounter;
    @NotBlank
    @HexString
    @Size(min = 6, max = 6)
    private String appCounter;
    @NotBlank
    @HexString
    @Size(min = 6, max = 6)
    private String mac;

    public String getStringData(){
        StringBuffer sb = new StringBuffer();
        sb.append(this.getCardNo());
        sb.append(this.getInstNo());
        sb.append(this.getChipSamUid());
        sb.append(this.getTerminalId());
        sb.append(this.getTrxCode());
        sb.append(this.getAmount());
        sb.append(this.getLastBalance());
        sb.append(this.getTrxDateTime());
        sb.append(this.getMainCounter());
        sb.append(this.getAppCounter());
        sb.append(this.getMac());
        return sb.toString();
    }
}
