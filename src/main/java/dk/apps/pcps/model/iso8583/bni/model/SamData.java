package dk.apps.pcps.model.iso8583.bni.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SamData {
    //TapCash
    private String pairCode;
    private String unPairCode;
    private String extAuthCode;
    //EMoney
    private String pinNo;
    private String pinCode;
}
