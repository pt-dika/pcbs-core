package dk.apps.pcps.commonutils.print;


import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;


public class PrintReceiptLogo {
	public static Graphics2D printLogo(Graphics2D graph2D, int iPrintReceiptWidth, int iFontSize, String strBankAcqPrintReceiptLogo) throws IOException {		
	    byte[] baImage = Base64.getDecoder().decode(strBankAcqPrintReceiptLogo);
	    ByteArrayInputStream bais = new ByteArrayInputStream(baImage);
	    Image imgPrintReceiptLogo = ImageIO.read(bais);
	    int iPrintReceiptLogoWidth = imgPrintReceiptLogo.getWidth(null);
	    int iCenterStart = iPrintReceiptWidth/2 - iPrintReceiptLogoWidth/2;
	    
	    graph2D.drawImage(imgPrintReceiptLogo, iCenterStart, iFontSize*2, null);
	    
	    return graph2D;
	}
}
