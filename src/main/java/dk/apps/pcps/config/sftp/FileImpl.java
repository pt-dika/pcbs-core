package dk.apps.pcps.config.sftp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@Slf4j
@Service
public class FileImpl implements FileService {

    @Override
    public boolean writeTxtFile(String filePath, String fileName, String content) {
        boolean isWriteFIle = false;
        try {
            File myObj = new File(filePath);
            if(!myObj.exists())
                myObj.mkdirs();
            if (!myObj.exists()) {
                myObj.createNewFile();
                log.info("File created: " + myObj.getName());
            } else {
                log.info("File modified");
            }
            FileWriter myWriter = new FileWriter(filePath+"/"+fileName);
            myWriter.write(content);
            myWriter.close();
            log.info("Successfully wrote to the file.");
            isWriteFIle = true;
        } catch (IOException e) {
            log.info("An error occurred.");
            e.printStackTrace();
        }
        return isWriteFIle;
    }

    @Override
    public String readTxtFile(String filePath, String fileName) {
        StringBuffer sbContent = null;
        try {
            File myObj = new File(filePath+fileName);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                sbContent.append(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            log.info("An error occurred.");
            e.printStackTrace();
        }
        return sbContent.toString() ;
    }

    @Override
    public List<String> getFileAndDirs(String filePath) {
        File dir = new File(filePath);
        return Arrays.asList(dir.list());
    }

    @Override
    public boolean removeDir(String localPath) {
        return false;
    }

    @Override
    public boolean removeFile(String localFilePath) {
        File file = new File(localFilePath);
        if(file.delete()) {
            log.info("File deleted successfully");
        }
        else {
            log.info("Failed to delete the file");
        }
        return false;
    }
}
