package dk.apps.pcps.db.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "merchant_and_batch_group")
@Data
public class MerchantAndBatchGroup {
    @Id
    private int id;
    private int merchantId;
    private String mid;
    private String additionalParam;
    private Boolean isDownloaded;
    private Timestamp updateAt;
    private Timestamp createAt;
    @ManyToOne
    @JoinColumn(name = "batch_group_id", referencedColumnName = "id", nullable = false)
    private BatchGroup batchGroup;
}
