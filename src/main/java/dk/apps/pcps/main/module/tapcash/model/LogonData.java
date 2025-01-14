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
public class LogonData {
    private String seed;
    private String tmk;
    private String rn;
    private String kek1;
    private String mack;
    private String mtmkIndex;
    private String parameterVersion;
    private String blacklistVersion;
    BlacklistData blacklistData;
    Object parameterData;
    SamData samData;
}
