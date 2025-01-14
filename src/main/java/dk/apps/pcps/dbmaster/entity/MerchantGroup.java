package dk.apps.pcps.dbmaster.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
@Data
public class MerchantGroup {
    @Id
    private int id;
    private String name;
    private String listenerUrl;
    private boolean listenerUrlEnabledFlag;
    private String tlsCa;
    private boolean tlsFlag;

}
