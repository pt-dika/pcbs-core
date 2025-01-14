package dk.apps.pcps.db.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "bin_and_batch_group_template_list")
@Data
public class BinAndBatchGroupTemplateList {
    @Id
    private int id;
    private Boolean isParentFlag;
    private int binId;
    private Timestamp updateAt;
    private Timestamp createAt;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "batch_group_list_template_id", referencedColumnName = "id", nullable = false)
    private BatchGroupListTemplate batchGroupListTemplate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "batch_group_id", referencedColumnName = "id", nullable = false)
    private BatchGroup batchGroup;
}
