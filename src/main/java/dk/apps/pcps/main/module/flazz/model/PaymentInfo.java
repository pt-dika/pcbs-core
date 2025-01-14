package dk.apps.pcps.main.module.flazz.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Size;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PaymentInfo {
    @Size(min = 2, max = 2)
    private String productId;
    @Size(min = 6, max = 6)
    private String purseExpiryDate;
    @Size(min = 16, max = 16)
    private String pursePan;
    @Size(min = 12, max = 12)
    private String purseBalance;
    @Size(min = 10, max = 10)
    private String purseId;
    @Size(min = 16, max = 16)
    private String rTerm;
    @Size(min = 6, max = 6)
    private String ctc;
    @Size(min = 8, max = 8)
    private String cCard;
    @Size(min = 16, max = 16)
    private String cTerm;
    @Size(min = 8, max = 8)
    private String ttc;
    @Size(min = 4, max = 4)
    private String cdc7;
    @Size(min = 16, max = 16)
    private String trn;
    @Size(min = 4, max = 4)
    private String year;
    @Size(min = 8, max = 8)
    private String psamId;
    @Size(min = 2, max = 2)
    private String version;
    @Size(min = 20, max = 20)
    private String otherData;

    public String getData(){
        StringBuffer sb = new StringBuffer();
        sb.append(this.getProductId());
        sb.append(this.getPurseExpiryDate());
        sb.append(this.getPursePan());
        sb.append(this.getPurseBalance());
        sb.append(this.getPurseId());
        sb.append(this.getRTerm());
        sb.append(this.getCtc());
        sb.append(this.getCCard());
        sb.append(this.getCTerm());
        sb.append(this.getTtc());
        sb.append(this.getCdc7());
        sb.append(this.getTrn());
        sb.append(this.getYear());
        sb.append(this.getPsamId());
        sb.append(this.getVersion());
        sb.append(this.getOtherData());
        return sb.toString();
    }
}
