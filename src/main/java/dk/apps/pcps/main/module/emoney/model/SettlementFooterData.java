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
public class SettlementFooterData {
    private String operator;
    private String totalRecord;

    public String getData(){
        StringBuffer sb = new StringBuffer();
        sb.append(this.getOperator());
        sb.append(this.getTotalRecord());
        return sb.toString();
    }
}
