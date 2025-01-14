package dk.apps.pcps.model.result.receipt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import dk.apps.pcps.model.result.SettlementSummaryReceiptData;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReceiptSettlementData {
    private String tid;
    private String mid;
    private String date;
    private String time;
    private String batch;
    private String host;
    private String issuer;
    List<SettlementSummaryReceiptData> summaryList;
}
