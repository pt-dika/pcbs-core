package dk.apps.pcps.config.sftp;

import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.Vector;

@Slf4j
@Service
public class FileTransferImpl implements FileTransferService {

    SftpProperties sftpProperties;

    public FileTransferImpl(SftpProperties sftpProperties){
        this.sftpProperties = sftpProperties;
    }

    @Override
    public void connect(String host, int port, String username, String password) {
        sftpProperties.setHost(host);
        sftpProperties.setPort(port);
        sftpProperties.setUsername(username);
        sftpProperties.setPassword(password);
    }

    @Override
    public boolean uploadFile(String localFilePath, String remoteFilePath) {
        ChannelSftp channelSftp = createChannelSftp();
        try {
            channelSftp.put(localFilePath, remoteFilePath);
            return true;
        } catch(SftpException ex) {
            log.error("Error upload file");
        } finally {
            disconnectChannelSftp(channelSftp);
        }

        return false;
    }

    @Override
    public boolean downloadFile(String localFilePath, String remoteFilePath) {
        ChannelSftp channelSftp = createChannelSftp();
        OutputStream outputStream;
        try {
            File file = new File(localFilePath);
            outputStream = new FileOutputStream(file);
            channelSftp.get(remoteFilePath, outputStream);
            file.createNewFile();
            return true;
        } catch(SftpException | IOException ex) {
            log.error("Error download file", ex);
        } finally {
            disconnectChannelSftp(channelSftp);
        }

        return false;
    }

    @Override
    public List<String> getFileAndDirs(String localFilePath) {
        ChannelSftp channelSftp = createChannelSftp();
        Vector v = null;
        try {
            v = channelSftp.ls(localFilePath);
        } catch (SftpException e) {
            e.printStackTrace();
        }
        return v;
    }

    @Override
    public boolean createDir(String remoteDir) {
        ChannelSftp channelSftp = createChannelSftp();
        try {
            channelSftp.mkdir(remoteDir);
            return true;
        } catch(SftpException ex) {
            log.error("Error create dir");
        } finally {
            disconnectChannelSftp(channelSftp);
        }

        return false;
    }

    private ChannelSftp createChannelSftp() {
        try {
            JSch jSch = new JSch();
            Session session = jSch.getSession(sftpProperties.getUsername(), sftpProperties.getHost(), sftpProperties.getPort());
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(sftpProperties.getPassword());
            session.connect(sftpProperties.getSessionTimeout());
            Channel channel = session.openChannel("sftp");
            channel.connect(sftpProperties.getChannelTimeout());
            return (ChannelSftp) channel;
        } catch(JSchException ex) {
            log.error("Create ChannelSftp error", ex);
        }

        return null;
    }

    private void disconnectChannelSftp(ChannelSftp channelSftp) {
        try {
            if( channelSftp == null)
                return;

            if(channelSftp.isConnected())
                channelSftp.disconnect();

            if(channelSftp.getSession() != null)
                channelSftp.getSession().disconnect();

        } catch(Exception ex) {
            log.error("SFTP disconnect error", ex);
        }
    }

}
