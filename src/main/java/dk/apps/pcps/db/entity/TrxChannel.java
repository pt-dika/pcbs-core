package dk.apps.pcps.db.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "trx_channel")
@Data
public class TrxChannel {
    @Id
    private Integer id;
    private String name;
    private String trxChannelMode;
    private String url;
    private String host;
    private Integer port;
    private String nii;
    private Timestamp updateAt;
    private Timestamp createAt;
    @ManyToOne
    @JoinColumn(name = "batch_group_id", referencedColumnName = "id", nullable = false)
    private BatchGroup batchGroup;
    @ManyToOne
    @JoinColumn(name = "cryptography_id", referencedColumnName = "id", nullable = false)
    private Cryptography cryptography;
    private String additionalParam;
}
