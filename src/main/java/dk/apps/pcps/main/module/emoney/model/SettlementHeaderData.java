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
public class SettlementHeaderData {
    private String cardType;
    private String numOfTrx;
    private String totalAmount;
    private String shiftId;
    private String operator;
    private String date;
    private String endOfChar;

    public String getData(){
        StringBuffer sb = new StringBuffer();
        sb.append(this.getCardType());
        sb.append(this.getNumOfTrx());
        sb.append(this.getTotalAmount());
        sb.append(this.getShiftId());
        sb.append(this.getOperator());
        sb.append(this.getDate());
        sb.append(this.getEndOfChar());
        return sb.toString();
    }

}
