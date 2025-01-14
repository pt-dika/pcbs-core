package dk.apps.pcps.main.model.result;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SettlementDetail {
    private String hostDate;
    private String hostTime;
    private String stan;
    private String batchNumber;
    private Integer numOfTransaction;
    private long totalBaseAmount;
}
