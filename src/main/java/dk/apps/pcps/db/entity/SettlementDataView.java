package dk.apps.pcps.db.entity;

import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Immutable
@Table(name = "settlement_data_view")
@Data
public class SettlementDataView {
    private int id;
    private String trxType;
    private String username;
    private String mid;
    private String tid;
    private long baseAmount;
    private double batchGroupMdrValue;
    private double merchantMdrValue;
    private double salesMdrValue;
    private String binResultName;
    private String stan;
    @Id
    private int invoiceNum;
    private String approvalCode;
    private String rrn;
    private byte firstCopyFlag;
    private String printReceiptEmail;
    private byte printReceiptEmailFlag;
    private String printReceiptPhoneNum;
    private byte printReceiptPhoneNumFlag;
    private String amountKsnIndex;
    private String hostDate;
    private String hostTime;
    private String additionalData;
    private Timestamp updateAt;
    private Timestamp createAt;
    private long batchGroupAdminFee;
    private long merchantAdminFee;
    private long salesAdminFee;
    private int salesId;
    private String posRequestType;
    private String posCloudPointer;
    private boolean hasBeenSentToMg;
    private String can;
    private String maskedCan;
    private boolean refund;
    @ManyToOne
    @JoinColumn(name = "batch_group_id", referencedColumnName = "id", nullable = false)
    private BatchGroup batchGroup;
    @ManyToOne
    @JoinColumn(name = "trx_channel_id", referencedColumnName = "id", nullable = false)
    private TrxChannel trxChannel;
}
