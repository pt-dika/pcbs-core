package dk.apps.pcps.model.result.receipt;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.experimental.Accessors;
import net.minidev.json.annotate.JsonIgnore;

@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReceiptHeader {
    private String bankCode;
    @JsonIgnore
    private String logo;
    private int merchantId;
    private String merchantName;
    private String address;
    private String address2;
    private boolean copy;
}
