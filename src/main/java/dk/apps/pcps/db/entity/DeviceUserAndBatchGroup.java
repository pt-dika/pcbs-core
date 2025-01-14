package dk.apps.pcps.db.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "device_user_and_batch_group")
@Data
public class DeviceUserAndBatchGroup {
    @Id
    private int id;
    private String username;
    private int batchNum;
    private String tid;
    private int rrn;
    private String additionalParam;
    private Timestamp updateAt;
    private Timestamp createAt;
    @ManyToOne
    @JoinColumn(name = "batch_group_id", referencedColumnName = "id", nullable = false)
    private BatchGroup batchGroup;
    private Timestamp lastSettlementAt;
    private int sessionNum;
}
