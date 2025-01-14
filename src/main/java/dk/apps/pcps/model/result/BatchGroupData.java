package dk.apps.pcps.model.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import dk.apps.pcps.model.payload.TrxHistoryData;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BatchGroupData {
    private int id;
    private String tid;
    private String mid;
    private String host;
    private String batchNum;
    private String name;
    private int trxCount;
    private Long totalAmount;
    private List<SuccessPaymentData> successPaymentList;
    private List<TrxHistoryData> trxHistoryList;
    private List<SettlementDetail> settlementDetails;
}
