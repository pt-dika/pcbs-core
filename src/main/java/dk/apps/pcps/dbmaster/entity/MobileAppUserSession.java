package dk.apps.pcps.dbmaster.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table
@Data
public class MobileAppUserSession {
    @Id
    private String username;
    private String hitFrom;
    private String token;
    private String signingKey;
    private Timestamp updateAt;
}
