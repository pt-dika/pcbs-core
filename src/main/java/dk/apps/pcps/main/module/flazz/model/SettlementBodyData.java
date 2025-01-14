package dk.apps.pcps.main.module.flazz.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SettlementBodyData extends FlazzTrxData {

    public String getData(){
        StringBuffer sb = new StringBuffer();
        sb.append(this.getRecordType());
        sb.append(this.getCardNo());
        sb.append(this.getExpiryDate());
        sb.append(this.getAmount());
        sb.append(this.getTraceNo());
        sb.append(this.getTrxDate());
        sb.append(this.getTrxTime());
        sb.append(this.getPosEntryMode());
        sb.append(this.getNii());
        sb.append(this.getPosConditionCode());
        sb.append(this.getTerminalId());
        sb.append(this.getMerchantId());
        sb.append(this.getInvoiceNo());
        sb.append(this.getUpdateStatus());
        sb.append(this.getPaymentInfo().getData());
        return sb.toString();
    }
}
