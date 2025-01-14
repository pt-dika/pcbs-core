package dk.apps.pcps.db.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table
@Data
public class Refund {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String mid;
    private String tid;
    private long baseAmount;
    private String maskedCan;
    private String binResultName;
    private double batchGroupMdrValue;
    private double merchantMdrValue;
    private double salesMdrValue;
    private int stan;
    private int invoiceNum;
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
    private String refundHostDate;
    private String refundHostTime;
    private String additionalData;
    private long batchGroupAdminFee;
    private long merchantAdminFee;
    private long salesAdminFee;
    private int salesId;
    private String posRequestType;
    private String posCloudPointer;
    private Boolean posCloudPushNotifFlag;
    private Boolean hasBeenSentToMg;
    private String can;
//    private String transType;
//    private String transHeader;
//    private String transData;
//    private String lastCounterData;
//    private String lastCreditHeader;
//    private String lastDebitOption;
//    private String lastPurseBalance;
//    private String lastPurseStatus;
//    private String lastTransRecord;
//    private String lastTransSignCert;
//    private String lastBdc;
//    private String lastCreditTrp;
//    private String lastTransTrp;
//    private String signCert;
    private Boolean refund;
    private Timestamp paidAt;
    private Timestamp updateAt;
    private Timestamp createAt;
    @ManyToOne
    @JoinColumn(name = "batch_group_id", referencedColumnName = "id", nullable = false)
    private BatchGroup batchGroup;
    @ManyToOne
    @JoinColumn(name = "trx_channel_id", referencedColumnName = "id", nullable = false)
    private TrxChannel trxChannel;
}
