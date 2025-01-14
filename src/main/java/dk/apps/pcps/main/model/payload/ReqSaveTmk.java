package dk.apps.pcps.main.model.payload;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ReqSaveTmk {
    private int batchGroupId;
    private String seed;
    private String tmk;
    private String mtmkIndex;
}
