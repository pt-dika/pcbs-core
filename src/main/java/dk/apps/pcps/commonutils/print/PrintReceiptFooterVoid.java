package dk.apps.pcps.commonutils.print;


import dk.apps.pcps.config.PCPSConfig;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;


public class PrintReceiptFooterVoid {
	public static MultipleReturnResult printFooter(Graphics2D graph2D, 
			String strBankIDAcqName, 
			int iPrintReceiptWidth, int iFontNormalSize, Font fontNormalSizePlain,
			Font fontNormalSizeItalic, int iRowIdx, int iBlankSpace,
			String strCustomerSignature, boolean boolCustomerSignatureFlag) throws IOException {
		
	    drawCenteredString(graph2D, "CUSTOMER HAS INCURRED THE", iPrintReceiptWidth, iFontNormalSize*(iRowIdx+=2), fontNormalSizePlain);
	    drawCenteredString(graph2D, "CHARGES HEREIN AND WILL OBSERVE", iPrintReceiptWidth, iFontNormalSize*(iRowIdx+=1), fontNormalSizePlain);
	    drawCenteredString(graph2D, "CUSTOMER AGREEMENT WITH THE CARD ISSUER", iPrintReceiptWidth, iFontNormalSize*(iRowIdx+=1), fontNormalSizePlain);
	    drawCenteredString(graph2D, "CUSTOMER ACKNOWLEDGE SATISFACTORY RECEIPT", iPrintReceiptWidth, iFontNormalSize*(iRowIdx+=1), fontNormalSizePlain);
	    drawCenteredString(graph2D, "OF RELATIVE GOOD SERVICES NO REFUND", iPrintReceiptWidth, iFontNormalSize*(iRowIdx+=1), fontNormalSizePlain);
    	drawCenteredString(graph2D, "***SIGNATURE REQUIRED***", iPrintReceiptWidth, iFontNormalSize*(iRowIdx+=1), fontNormalSizePlain);
    	if (boolCustomerSignatureFlag==true) {
	    	if (strCustomerSignature.length()>0) {
	        	byte[] baCustomerSignature = Base64.getDecoder().decode(strCustomerSignature);
		    	Image imgCustomerSignature = ImageIO.read(new ByteArrayInputStream(baCustomerSignature));
		    	imgCustomerSignature = createResizedCopy(imgCustomerSignature, imgCustomerSignature.getWidth(null)/2, iFontNormalSize*5, false);	
		    	int iImgCustomerSignatureWidth = imgCustomerSignature.getWidth(null);
			    int iCenterStart = iPrintReceiptWidth/2 - iImgCustomerSignatureWidth/2;
			    graph2D.drawImage(imgCustomerSignature, iCenterStart, iFontNormalSize*(iRowIdx+=1), null);
	    	}
    	}
    	drawCenteredString(graph2D, "________________________", iPrintReceiptWidth, iFontNormalSize*(iRowIdx+=5), fontNormalSizePlain);
	    
	    if (strBankIDAcqName.length()!=0) {
	    	drawCenteredString(graph2D, "ACQUIRED BY " + strBankIDAcqName , iPrintReceiptWidth, iFontNormalSize*(iRowIdx+=2), fontNormalSizePlain);
	    }
	    drawCenteredString(graph2D, PCPSConfig.PRINT_RECEIPT_CREDIT_LIST, iPrintReceiptWidth, iFontNormalSize*(iRowIdx+=2), fontNormalSizePlain);

	    //graph2D.setFont(fontNormalSizeItalic);
	    //graph2D.drawString("credit_debit_cp_msg_frm_ver: " + CreditDebitCPParam.msg_format_version, iFontNormalSize*iBlankSpace, iFontNormalSize*(iRowIdx+=2));
	    //graph2D.drawString("device_crypto_ver: " + CryptoParam.device_crypto_version, iFontNormalSize*iBlankSpace, iFontNormalSize*(iRowIdx+=1));
	    
	    return new MultipleReturnResult(graph2D, iRowIdx);
	}
	
    public static void drawCenteredString(Graphics2D g, String text, int width, int y, Font font) {
        // Get the FontMetrics
        FontMetrics metrics = g.getFontMetrics(font);
        // Determine the X coordinate for the text
        int x = (width - metrics.stringWidth(text)) / 2;
        // Set the font
        g.setFont(font);
        // Draw the String
        g.drawString(text, x, y);
    }
    
    public static Image createResizedCopy(Image originalImage, int scaledWidth, int scaledHeight, boolean preserveAlpha) {
        System.out.println("resizing...");
        int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
        Graphics2D g = scaledBI.createGraphics();
        if (preserveAlpha) {
            g.setComposite(AlphaComposite.Src);
        }
        g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null); 
        g.dispose();
        
        return scaledBI;
    }
}
