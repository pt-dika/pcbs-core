package dk.apps.pcps.db.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table
@Data
public class Cryptography {
    @Id
    private int id;
    private String name;
    private Timestamp updateAt;
    private Timestamp createAt;
}
