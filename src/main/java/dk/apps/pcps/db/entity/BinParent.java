package dk.apps.pcps.db.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "bin_parent")
@Data
public class BinParent {
    @Id
    private int id;
    private String name;
    private String aid;
    private String binRangeStart;
    private String binRangeEnd;
    private Timestamp updateAt;
    private Timestamp createAt;
}
