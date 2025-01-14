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
public class SettlementFileNameData {
    private String operator;
    private String shiftId;
    private String branchCode;
    private String gate;
    private String substation;
    private boolean newSettle;
    private String seq;
    private String settleDateTime;
    private String ds;

    public String getData(){
        StringBuffer sb = new StringBuffer();
        sb.append(this.operator);
        sb.append(this.shiftId);
        sb.append(this.branchCode);
        sb.append(this.gate);
        sb.append(this.substation);
        if (this.isNewSettle()){
            sb.append("FF");
        }
        sb.append(this.seq);
        sb.append(this.settleDateTime);
        sb.append(this.settleDateTime);
        sb.append("DS");
        return sb.toString();
    }
}
