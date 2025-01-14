package dk.apps.pcps.db.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;


@Entity
@Table(name = "merchant_mdr")
@Data
public class MerchantMdr {
    @Id
    private int id;
    private int merchantId;
    private Long merchantMdr;
    private Long merchantAdminFee;
    private Long salesMdr;
    private long salesAdminFee;
    private Timestamp updateAt;
    private Timestamp createAt;
    @ManyToOne
    @JoinColumn(name = "batch_group_id", referencedColumnName = "id", nullable = false)
    private BatchGroup batchGroup;
}
