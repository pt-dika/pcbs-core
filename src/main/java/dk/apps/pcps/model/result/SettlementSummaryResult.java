package dk.apps.pcps.model.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import dk.apps.pcps.model.result.receipt.ReceiptSettlementData;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;
import java.util.List;

@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SettlementSummaryResult {
    private Integer sessionNumber;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+7")
    private Timestamp lastSettlement;
    private List<SettlementSessionData> settlementSessionList;
    private List<BatchGroupData> settlementSummaryList;
    private ReceiptSettlementData settlementSummary;
}
