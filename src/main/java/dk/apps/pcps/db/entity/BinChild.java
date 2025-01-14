package dk.apps.pcps.db.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "bin_child")
@Data
public class BinChild {
    @Id
    private int id;
    private String name;
    private int bankId;
    private String aid;
    private String binRangeStart;
    private String binRangeEnd;
    private Timestamp updateAt;
    private Timestamp createAt;
    @ManyToOne
    @JoinColumn(name = "bin_parent_id", referencedColumnName = "id", nullable = false)
    private BinParent binParent;
}
