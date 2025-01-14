package dk.apps.pcps.db.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "trx_history")
@Data
public class TrxHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String trxType;
    private String username;
    private String mid;
    private String tid;
    private long baseAmount;
    private String maskedCan;
    private String binResultName;
    private double batchGroupMdrValue;
    private double merchantMdrValue;
    private double salesMdrValue;
    private String stan;
    private int batchNum;
    private int invoiceNum;
    private String approvalCode;
    private String rrn;
    private String printReceiptEmail;
    private String printReceiptPhoneNum;
    private String paymentHostDate;
    private String paymentHostTime;
    private String refundHostDate;
    private String refundHostTime;
    private long batchGroupAdminFee;
    private long merchantAdminFee;
    private long salesAdminFee;
    private Integer salesId;
    private String posRequestType;
    private String posCloudPointer;
    private Boolean posCloudPushNotifFlag;
    private Boolean hasBeenSentToMg;
    private String additionalData;
    private Timestamp paidAt;
    private Timestamp refundAt;
    private Timestamp updateAt;
    private Timestamp createAt;
    @ManyToOne
    @JoinColumn(name = "batch_group_id", referencedColumnName = "id", nullable = false)
    private BatchGroup batchGroup;
    @ManyToOne
    @JoinColumn(name = "trx_channel_id", referencedColumnName = "id", nullable = false)
    private TrxChannel trxChannel;
}
