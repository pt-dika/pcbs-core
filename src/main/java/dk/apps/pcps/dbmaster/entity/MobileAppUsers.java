package dk.apps.pcps.dbmaster.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table
@Data
public class MobileAppUsers {
    private int id;
    @Id
    private String username;
    private String passwdHash;
    private String firstName;
    private String lastName;
    private String address;
    private String printReceiptMerchantName;
    @Column(name = "print_receipt_address_line_1")
    private String printReceiptAddressLine1;
    @Column(name = "print_receipt_address_line_2")
    private String printReceiptAddressLine2;
    private int stan;
    private int invoiceNum;
    private Timestamp lastUserDataUpdateTimestamp;
    private Boolean lockFlag;
    //private String posRequestType;
    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "merchant_id", referencedColumnName = "id")
    private Merchant merchant;
}
