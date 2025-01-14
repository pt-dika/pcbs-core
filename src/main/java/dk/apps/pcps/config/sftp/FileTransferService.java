package dk.apps.pcps.config.sftp;

import java.util.List;

public interface FileTransferService {
    void connect(String host, int port, String username, String password);
    boolean uploadFile(String localFilePath, String remoteFilePath);
    boolean downloadFile(String localFilePath, String remoteFilePath);
    List<String> getFileAndDirs(String localFilePath);
    boolean createDir(String remoteDir);
}
