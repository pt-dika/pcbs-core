package dk.apps.pcps.main.model.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessPaymentSummary {
    private int batchGroupId;
    private String tid;
    private String mid;
    private String batchNum;
    private String name;
    private int trxCount;
    private Long totalAmount;
    private String detailUrl;
    private boolean isSettlement;
    private String settlementStatus;
}
