package dk.apps.pcps.main.model.result;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RefundSummary {
    private Integer batchNumber;
    private Integer numOfPayment;
    private Long totalBaseAmount;
}
