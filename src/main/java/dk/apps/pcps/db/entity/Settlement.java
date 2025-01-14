package dk.apps.pcps.db.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table
@Data
public class Settlement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    @ManyToOne
    @JoinColumn(name = "batch_group_id", referencedColumnName = "id", nullable = false)
    private BatchGroup batchGroup;
    private int batchNum;
    private String stan;
    private int numOfSuccessPayment;
    private long successPaymentTotalBaseAmount;
    private int numOfRefund;
    private long refundTotalBaseAmount;
    private int numOfTips;
    private long tipsTotalBaseAmount;
    private String settlementHostDate;
    private String settlementHostTime;
    private int settlementSessionNum;
    private Boolean hasBeenSentToMg;
    private String additionalParam;
    private Timestamp updateAt;
    private Timestamp createAt;
    @ManyToOne
    @JoinColumn(name = "trx_channel_id", referencedColumnName = "id", nullable = false)
    private TrxChannel trxChannel;
}
