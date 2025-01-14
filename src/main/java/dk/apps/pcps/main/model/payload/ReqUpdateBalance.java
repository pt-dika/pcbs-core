package dk.apps.pcps.main.model.payload;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.Valid;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ReqUpdateBalance<T> extends MerchantAndUser {
    private String can;
    private boolean reversal;
    private String stan;
    @Valid
    private T cardData;
}
