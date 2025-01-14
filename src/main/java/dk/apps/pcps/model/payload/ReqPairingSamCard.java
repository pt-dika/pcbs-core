package dk.apps.pcps.model.payload;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import dk.apps.pcps.validation.constraint.HexString;

import javax.validation.constraints.NotBlank;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ReqPairingSamCard {
    private int batchGroupId;
    @NotBlank
    @HexString
    private String pairCode;
}
