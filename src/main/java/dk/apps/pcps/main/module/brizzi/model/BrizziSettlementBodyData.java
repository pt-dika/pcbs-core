package dk.apps.pcps.main.module.brizzi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BrizziSettlementBodyData extends BrizziTrxData {
    private String mid;
    private String tid;

    public String getData(){
        StringBuffer sb = new StringBuffer();
        sb.append(this.getCardNo());
        sb.append(this.getTrxDate());
        sb.append(this.getTrxTime());
        sb.append(this.getAmount());
        sb.append(this.getRefNum());
        sb.append(this.getBatchNum());
        sb.append(this.getMid());
        sb.append(this.getTid());
        sb.append(this.getHash());
        return sb.toString();
    }
}
