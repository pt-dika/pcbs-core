package dk.apps.pcps.model.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import dk.apps.pcps.model.iso8583.bni.model.SamData;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BinAllowData {
    private int batchGroupId;
    private String samPairCode;
    private String batchNum;
    private String mid;
    private String tid;
    private String aid;
    private String binName;
    private String channelName;
    private String bankName;
    private String prepaidCardName;
    private String bankLogo;
    private String prepaidLogo;
    private SamData samData;
}
