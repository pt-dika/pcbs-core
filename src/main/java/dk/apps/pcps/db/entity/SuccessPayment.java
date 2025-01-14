package dk.apps.pcps.db.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "success_payment")
@Data
public class SuccessPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String mid;
    private String tid;
    private long baseAmount;
    private long currentBalance;
    private long lastBalance;
    private String can;
    private double batchGroupMdrValue;
    private double merchantMdrValue;
    private double salesMdrValue;
    private String stan;
    private Integer invoiceNum;
    private String approvalCode;
    private String rrn;
    private Boolean firstCopyFlag;
    private String printReceiptEmail;
    private Boolean printReceiptEmailFlag;
    private String printReceiptPhoneNum;
    private Boolean printReceiptPhoneNumFlag;
    private String amountKsnIndex;
    private String paymentHostDate;
    private String paymentHostTime;
    private String additionalData;
    private String binResultName;
    private long batchGroupAdminFee;
    private long merchantAdminFee;
    private long salesAdminFee;
    private int salesId;
    private String posRequestType;
    private String posCloudPointer;
    private boolean posCloudPushNotifFlag;
    private boolean hasBeenSentToMg;
    private boolean refund;
    private Timestamp updateAt;
    private Timestamp createAt;
    @ManyToOne
    @JoinColumn(name = "batch_group_id", referencedColumnName = "id", nullable = false)
    private BatchGroup batchGroup;
    @ManyToOne
    @JoinColumn(name = "trx_channel_id", referencedColumnName = "id", nullable = false)
    private TrxChannel trxChannel;
}
