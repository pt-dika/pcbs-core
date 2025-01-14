package dk.apps.pcps.dbmaster.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table
@Data
public class DeviceUserTle {
    @Id
    private int id;
    private String tid;
    private String mid;
    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "bank_id", referencedColumnName = "id")
    private Bank bank;
    private String tleMkId;
    private String tleMk;
    private String tleWkId;
    private String tleWkDek;
    private String tleWkMak;
    private Timestamp updateAt;
    private Timestamp createAt;
}
