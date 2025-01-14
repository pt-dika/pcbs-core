package dk.apps.pcps.dbmaster.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
@Data
public class Bank {
    @Id
    private int id;
    private String code;
    private String name;
    private String printReceiptLogo;
}
