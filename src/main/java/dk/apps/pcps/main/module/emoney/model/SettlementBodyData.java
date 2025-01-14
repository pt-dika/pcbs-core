package dk.apps.pcps.main.module.emoney.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SettlementBodyData extends TrxData{
    private String shift;
    private String seqNo;

    public String getData(){
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
