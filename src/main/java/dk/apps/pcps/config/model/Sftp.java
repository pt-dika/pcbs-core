package dk.apps.pcps.config.model;

import lombok.Data;

@Data
public class Sftp {
    private String username;
    private String password;
    private String path;

}
