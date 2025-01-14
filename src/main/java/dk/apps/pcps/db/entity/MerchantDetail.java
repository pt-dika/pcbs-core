package dk.apps.pcps.db.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;

@Entity
@Table(name = "merchant_detail")
@Data
public class MerchantDetail {
    @Id
    private int id;
    private int merchantId;
    private Time settlementDelayTime;
    private long minimumAmount;
    private long maximumAmount;
    private Boolean lockFlag;
    private String additionalParam;
    private Timestamp updateAt;
    private Timestamp createAt;
    @ManyToOne
    @JoinColumn(name = "batch_group_list_template_id", referencedColumnName = "id", nullable = false)
    private BatchGroupListTemplate batchGroupListTemplate;
}
