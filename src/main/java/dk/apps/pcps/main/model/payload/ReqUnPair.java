package dk.apps.pcps.main.model.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import dk.apps.pcps.validation.constraint.HexString;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ReqUnPair {
    private Integer batchGroupId;
    @NotBlank
    @HexString
    @Size(min = 16, max = 16)
    private String samId;
    @NotBlank
    @HexString
    @Size(min = 16, max = 16)
    private String crn;
    @NotBlank
    @HexString
    @Size(min = 16, max = 16)
    private String trn;
}
