package dk.apps.pcps.main.model.payload;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ReqPrintReceipt {
    private int batchGroupId;
    private int invoiceNum;
    private int sessionNum;
}
