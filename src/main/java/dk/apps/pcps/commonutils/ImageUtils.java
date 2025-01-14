package dk.apps.pcps.commonutils;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public class ImageUtils {
    public static byte[] append(List<BufferedImage> bufferedImages, String outputFileName, boolean isX) {
        if (bufferedImages == null || bufferedImages.size() == 0) {
            return null;
        }

        try {
            boolean isFirstPng = true;
            BufferedImage outputImg = null;
            int outputImgW = 0;
            int outputImgH = 0;
            for (BufferedImage bufferedImage : bufferedImages) {
                if (isFirstPng) {
                    isFirstPng = false;
                    outputImg = bufferedImage;
                    outputImgW = outputImg.getWidth();
                    outputImgH = outputImg.getHeight();
                } else {
                    BufferedImage appendImg = bufferedImage;
                    int appendImgW = appendImg.getWidth();
                    int appendImgH = appendImg.getHeight();

                    if (isX) {
                        outputImgW = outputImgW + appendImgW;
                        outputImgH = outputImgH > appendImgH ? outputImgH : appendImgH;
                    } else {
                        outputImgW = outputImgW > appendImgW ? outputImgW : appendImgW;
                        outputImgH = outputImgH + appendImgH;
                    }

                    // create basic image
                    Graphics2D g2d = outputImg.createGraphics();
                    BufferedImage imageNew = g2d.getDeviceConfiguration().createCompatibleImage(outputImgW, outputImgH,
                            Transparency.TRANSLUCENT);
                    g2d.dispose();
                    g2d = imageNew.createGraphics();

                    int oldImgW = outputImg.getWidth();
                    int oldImgH = outputImg.getHeight();
                    g2d.drawImage(outputImg, 0, 0, oldImgW, oldImgH, null);
                    if (isX) {
                        g2d.drawImage(appendImg, oldImgW, 0, appendImgW, appendImgH, null);
                    } else {
                        g2d.drawImage(appendImg, 0, oldImgH, appendImgW, appendImgH, null);
                    }
                    g2d.dispose();
                    outputImg = imageNew;
                }
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(outputImg, "png", baos);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
            return null;
        }
    }

    public static String encodeFileToBase64Binary(File file){
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int)file.length()];
            fileInputStreamReader.read(bytes);
            return new String(Base64.encodeBase64(bytes), "UTF-8");
        } catch (Exception e){
            return "";
        }
    }
}
