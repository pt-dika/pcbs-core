package dk.apps.pcps.main.model.result;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import dk.apps.pcps.db.entity.BatchGroup;
import dk.apps.pcps.db.entity.TrxChannel;
import dk.apps.pcps.dbmaster.entity.MobileAppUsers;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ChannelResult {
    private MobileAppUsers mobileAppUsers;
    private String binResultName;
    private BatchGroup batchGroup;
    private TrxChannel trxChannel;
}
