package dk.apps.pcps.db.entity;

import lombok.Data;
import dk.apps.pcps.commonutils.Utility;
import dk.apps.pcps.model.payload.AdditionalParam;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "device_user_detail")
@Data
public class DeviceUserDetail {
    @Id
    private int id;
    private String username;
    private int invoiceNum;
    private int settlementSessionNum;
    private Timestamp lastSettlementAt;
    private Boolean lockFlag;
    private String additionalParam;
    private Timestamp updateAt;
    private Timestamp createAt;


    public AdditionalParam getAdditionalParams(){
        return Utility.jsonToObject(this.additionalParam, AdditionalParam.class);
    }
}
