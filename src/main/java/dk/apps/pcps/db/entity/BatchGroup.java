package dk.apps.pcps.db.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "batch_group")
@Data
public class BatchGroup {

    @Id
    private int id;
    private String name;
    private String additionalParam;
    private Timestamp updateAt;
    private Timestamp createAt;
    private int bankId;
}
