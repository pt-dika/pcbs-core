package dk.apps.pcps.main.module.tapcash.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InquiryUpdateBalanceResult {
    private String stan;
    private String rrn;
    private boolean reversal;
    private String transactionLogRecord;
    private String creditCryptogram;
    private TransactionLogResult transactionLogResult;
}
