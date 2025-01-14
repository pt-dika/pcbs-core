package dk.apps.pcps.config.sftp;

import java.util.List;

public interface FileService {
    boolean writeTxtFile(String filePath, String fileName, String content);
    String readTxtFile(String filePath, String fileName);
    List<String> getFileAndDirs(String localFilePath);
    boolean removeDir(String localPath);
    boolean removeFile(String localFilePath);
}
