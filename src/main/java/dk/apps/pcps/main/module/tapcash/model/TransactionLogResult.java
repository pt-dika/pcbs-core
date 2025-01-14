package dk.apps.pcps.main.module.tapcash.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TransactionLogResult {
    private String type;
    private String amount;
    private String txnDateTime;
    private String userData;
}
