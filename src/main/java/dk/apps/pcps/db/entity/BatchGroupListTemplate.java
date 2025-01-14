package dk.apps.pcps.db.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "batch_group_list_template")
@Data
public class BatchGroupListTemplate {
    @Id
    private int id;
    private String name;
    private Timestamp updateAt;
    private Timestamp createAt;
}
