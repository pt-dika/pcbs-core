package dk.apps.pcps.db.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table
public class SettlementFileUpload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String invoiceNums;
    private int batchGroupId;
    private int page;
    private String fileName;
    private String header;
    private String body;
    private String footer;
    private String username;
    private String status;
    private Timestamp createAt;
    private long totalAmount;
    private boolean isSettle;
}
