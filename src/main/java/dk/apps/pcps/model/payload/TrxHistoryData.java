package dk.apps.pcps.model.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import dk.apps.pcps.db.entity.BatchGroup;
import dk.apps.pcps.db.entity.TrxChannel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TrxHistoryData {
    private String invoiceNum;
    private String stan;
    private int batchNum;
    private String mid;
    private String tid;
    private String trxType;
    private long baseAmount;
    private String approvalCode;
    private String rrn;
    private String hostDate;
    private String hostTime;
    @JsonIgnore
    private String binResultName;
    @JsonIgnore
    private String username;
    @JsonIgnore
    private String additionalData;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+7")
    private Timestamp updateAt;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+7")
    private Timestamp createAt;
    @JsonIgnore
    private long batchGroupAdminFee;
    @JsonIgnore
    private long merchantAdminFee;
    @JsonIgnore
    private long salesAdminFee;
    @JsonIgnore
    private int salesId;
    @JsonIgnore
    private String posRequestType;
    @JsonIgnore
    private String posCloudPointer;
    @JsonIgnore
    private boolean hasBeenSentToMg;
    private String maskedCan;
    private String can;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+7")
    private Timestamp paidAt;
    @JsonIgnore
    private BatchGroup batchGroup;
    @JsonIgnore
    private TrxChannel trxChannel;
}

