package dk.apps.pcps.main.model.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;
import java.util.List;

@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SettlementSessionData {
    private Integer sessionNumber;
    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss", timezone="GMT+7")
    private Timestamp settlementAt;
    private List<BatchGroupData> settlementSummaryList;
}
