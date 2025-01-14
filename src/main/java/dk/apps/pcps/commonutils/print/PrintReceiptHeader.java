package dk.apps.pcps.commonutils.print;


import java.awt.*;
import java.io.IOException;


public class PrintReceiptHeader {
	public static MultipleReturnResult printHeader(
			Graphics2D graph2D, 
			String strPrintReceiptMerchantName, String strPrintReceiptAddressLine1,
			String strPrintReceiptAddressLine2, String strMerchantName,
			long lMerchantID,
			int iPrintReceiptWidth, int iFontSize,
			Font font, int iRowIdx) throws IOException {
		
		drawCenteredString(graph2D, strPrintReceiptMerchantName.toUpperCase(), iPrintReceiptWidth, iFontSize*(iRowIdx+=1), font);
	    drawCenteredString(graph2D, strPrintReceiptAddressLine1.toUpperCase(), iPrintReceiptWidth, iFontSize*(iRowIdx+=1), font);
	    drawCenteredString(graph2D, strPrintReceiptAddressLine2.toUpperCase(), iPrintReceiptWidth, iFontSize*(iRowIdx+=1), font);
	    drawCenteredString(graph2D, "MERC" + lMerchantID, iPrintReceiptWidth, iFontSize*(iRowIdx+=1), font);
	    
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
}
