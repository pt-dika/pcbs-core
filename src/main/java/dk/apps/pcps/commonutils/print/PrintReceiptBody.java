package dk.apps.pcps.commonutils.print;

import dk.apps.pcps.config.PCPSConfig;
import dk.apps.pcps.model.result.SettlementSummaryReceiptData;
import dk.apps.pcps.model.result.receipt.ReceiptPurchaseData;
import dk.apps.pcps.model.result.receipt.ReceiptSettlementData;

import java.awt.*;
import java.nio.CharBuffer;
import java.util.List;

public class PrintReceiptBody {


    public static MultipleReturnResult purchase(
            ReceiptPurchaseData receipt,
            Graphics2D graph2D, int iFontNormalSize,
            int iBlankSpace, Font fontNormalSizePlain, Font fontNormalSizeBold, int iRowIdx){

        int iSpaces = 0;
        String strParamBuff1 = "";
        String strParamBuff2 = "";
        int iPrintSpace = PCPSConfig.PRINT_RECEIPT_PRINTED_COLUMN;
        int iPrintReceiptWidth = iFontNormalSize*(iBlankSpace+iPrintSpace+iBlankSpace);
        graph2D.setFont(fontNormalSizePlain);

        String tid = receipt.getTid();
        String mid = receipt.getMid();
        String trxType = "PURCHASE";
        String can = receipt.getMaskedCan();
        String date = receipt.getDate();
        String time = receipt.getTime();
        String batch = receipt.getBatch();
        String invNo = receipt.getInvoiceNo();
        String ptc = receipt.getPtc();
        String apprCode = receipt.getApprovalCode();
        strParamBuff1 = "TID   : ";
        strParamBuff2 = "MID : ";
        iSpaces = (PCPSConfig.PRINT_RECEIPT_PRINTED_COLUMN + PCPSConfig.PRINT_RECEIPT_SHADOW_COLUMN)
                - strParamBuff1.length() - tid.length() - strParamBuff2.length() - mid.length();
        if (iSpaces<=0) {
            iSpaces = 1;
        }
        graph2D.drawString(strParamBuff1 + tid + spaces(iSpaces) + strParamBuff2 + mid, iFontNormalSize*iBlankSpace, iFontNormalSize*(iRowIdx+=1));

        graph2D.setFont(fontNormalSizeBold);
        graph2D.drawString(trxType, iFontNormalSize*iBlankSpace, iFontNormalSize*(iRowIdx+=1));
        strParamBuff1 = "BATCH : ";
        strParamBuff2 = "INV NO: ";
        iSpaces = (PCPSConfig.PRINT_RECEIPT_PRINTED_COLUMN + PCPSConfig.PRINT_RECEIPT_SHADOW_COLUMN)
                - strParamBuff1.length() - batch.length() - strParamBuff2.length() - invNo.length();
        if (iSpaces<=0) {
            iSpaces = 1;
        }
        graph2D.setFont(fontNormalSizePlain);
        graph2D.drawString(strParamBuff1 + batch + spaces(iSpaces) + strParamBuff2 + invNo, iFontNormalSize*iBlankSpace, iFontNormalSize*(iRowIdx+=1));


        strParamBuff1 = "DATE  : ";
        strParamBuff2 = "TIME : ";
        iSpaces = (PCPSConfig.PRINT_RECEIPT_PRINTED_COLUMN + PCPSConfig.PRINT_RECEIPT_SHADOW_COLUMN)
                - strParamBuff1.length() - date.length() - strParamBuff2.length() - time.length();
        if (iSpaces<=0) {
            iSpaces = 1;
        }
        graph2D.drawString(strParamBuff1 + date + spaces(iSpaces) + strParamBuff2 + time, iFontNormalSize*iBlankSpace, iFontNormalSize*(iRowIdx+=1));

        strParamBuff1 = "PTC   : ";
        strParamBuff2 = "APPR CODE: ";
        iSpaces = (PCPSConfig.PRINT_RECEIPT_PRINTED_COLUMN + PCPSConfig.PRINT_RECEIPT_SHADOW_COLUMN)
                - strParamBuff1.length() - ptc.length() - strParamBuff2.length() - apprCode.length();
        if (iSpaces<=0) {
            iSpaces = 1;
        }
        graph2D.drawString(strParamBuff1 + ptc + spaces(iSpaces) + strParamBuff2 + apprCode, iFontNormalSize*iBlankSpace, iFontNormalSize*(iRowIdx+=1));

        graph2D.drawString("CERTIFICATE DATA : "+receipt.getCertData(), iFontNormalSize*iBlankSpace, iFontNormalSize*(iRowIdx+=1));
        graph2D.drawString("COUNTER DATA     : "+receipt.getCounterData(), iFontNormalSize*iBlankSpace, iFontNormalSize*(iRowIdx+=1));
        graph2D.drawString("TRANS HEADER     : "+receipt.getTransHeader(), iFontNormalSize*iBlankSpace, iFontNormalSize*(iRowIdx+=1));
        graph2D.drawString("SIGN CERTIFICATE : "+receipt.getSignCert(), iFontNormalSize*iBlankSpace, iFontNormalSize*(iRowIdx+=1));
        graph2D.drawString("CAN              : "+receipt.getMaskedCan(), iFontNormalSize*iBlankSpace, iFontNormalSize*(iRowIdx+=1));
        graph2D.drawString("", iFontNormalSize*iBlankSpace, iFontNormalSize*(iRowIdx+=1));
        graph2D.drawString("Previous Balance : "+receipt.getPrevBal(), iFontNormalSize*iBlankSpace, iFontNormalSize*(iRowIdx+=1));
        graph2D.drawString("Amount           : "+receipt.getAmount(), iFontNormalSize*iBlankSpace, iFontNormalSize*(iRowIdx+=1));
        graph2D.drawString("Current Balance  : "+receipt.getCurrBal(), iFontNormalSize*iBlankSpace, iFontNormalSize*(iRowIdx+=1));

        iSpaces = (PCPSConfig.PRINT_RECEIPT_PRINTED_COLUMN + PCPSConfig.PRINT_RECEIPT_SHADOW_COLUMN)
                - strParamBuff1.length() - strParamBuff2.length();
        if (iSpaces<=0) {
            iSpaces = 1;
        }

        graph2D.drawString("==========================================", iFontNormalSize*iBlankSpace, iFontNormalSize*(iRowIdx+=2));
        graph2D.drawString("", iFontNormalSize*iBlankSpace, iFontNormalSize*(iRowIdx+=1));

        return new MultipleReturnResult(graph2D, iRowIdx);

    }

    public static MultipleReturnResult updateBalance(
            ReceiptPurchaseData receipt,
            Graphics2D graph2D, int iFontNormalSize,
            int iBlankSpace, Font fontNormalSizePlain, Font fontNormalSizeBold, int iRowIdx){

        int iSpaces = 0;
        String strParamBuff1 = "";
        String strParamBuff2 = "";
        int iPrintSpace = PCPSConfig.PRINT_RECEIPT_PRINTED_COLUMN;
        int iPrintReceiptWidth = iFontNormalSize*(iBlankSpace+iPrintSpace+iBlankSpace);
        graph2D.setFont(fontNormalSizePlain);

        String tid = receipt.getTid();
        String mid = receipt.getMid();
        String trxType = receipt.getTrxType();
        String can = receipt.getMaskedCan();
        String date = receipt.getDate();
        String time = receipt.getTime();
        String batch = receipt.getBatch();
        String invNo = receipt.getInvoiceNo();
        String ptc = receipt.getPtc();
        String apprCode = receipt.getApprovalCode();
        strParamBuff1 = "TID   : ";
        strParamBuff2 = "MID : ";
        iSpaces = (PCPSConfig.PRINT_RECEIPT_PRINTED_COLUMN + PCPSConfig.PRINT_RECEIPT_SHADOW_COLUMN)
                - strParamBuff1.length() - tid.length() - strParamBuff2.length() - mid.length();
        if (iSpaces<=0) {
            iSpaces = 1;
        }
        graph2D.drawString(strParamBuff1 + tid + spaces(iSpaces) + strParamBuff2 + mid, iFontNormalSize*iBlankSpace, iFontNormalSize*(iRowIdx+=1));

        graph2D.drawString(trxType, iFontNormalSize*iBlankSpace, iFontNormalSize*(iRowIdx+=1));
        graph2D.drawString(can, iFontNormalSize*iBlankSpace, iFontNormalSize*(iRowIdx+=1));
        strParamBuff1 = "BATCH : ";
        strParamBuff2 = "INV NO: ";
        iSpaces = (PCPSConfig.PRINT_RECEIPT_PRINTED_COLUMN + PCPSConfig.PRINT_RECEIPT_SHADOW_COLUMN)
                - strParamBuff1.length() - batch.length() - strParamBuff2.length() - invNo.length();
        if (iSpaces<=0) {
            iSpaces = 1;
        }
        graph2D.drawString(strParamBuff1 + batch + spaces(iSpaces) + strParamBuff2 + invNo, iFontNormalSize*iBlankSpace, iFontNormalSize*(iRowIdx+=1));


        strParamBuff1 = "DATE  : ";
        strParamBuff2 = "TIME : ";
        iSpaces = (PCPSConfig.PRINT_RECEIPT_PRINTED_COLUMN + PCPSConfig.PRINT_RECEIPT_SHADOW_COLUMN)
                - strParamBuff1.length() - date.length() - strParamBuff2.length() - time.length();
        if (iSpaces<=0) {
            iSpaces = 1;
        }
        graph2D.drawString(strParamBuff1 + date + spaces(iSpaces) + strParamBuff2 + time, iFontNormalSize*iBlankSpace, iFontNormalSize*(iRowIdx+=1));

        strParamBuff1 = "REF NO : ";
        strParamBuff2 = "APPR CODE: ";
        iSpaces = (PCPSConfig.PRINT_RECEIPT_PRINTED_COLUMN + PCPSConfig.PRINT_RECEIPT_SHADOW_COLUMN)
                - strParamBuff1.length() - ptc.length() - strParamBuff2.length() - apprCode.length();
        if (iSpaces<=0) {
            iSpaces = 1;
        }
        graph2D.drawString(strParamBuff1 + ptc + spaces(iSpaces) + strParamBuff2 + apprCode, iFontNormalSize*iBlankSpace, iFontNormalSize*(iRowIdx+=1));

        graph2D.drawString("", iFontNormalSize*iBlankSpace, iFontNormalSize*(iRowIdx+=1));
        graph2D.drawString("Previous Balance :", iFontNormalSize*iBlankSpace, iFontNormalSize*(iRowIdx+=1));
        graph2D.drawString("Update           :", iFontNormalSize*iBlankSpace, iFontNormalSize*(iRowIdx+=1));
        graph2D.drawString("Current Balance  :", iFontNormalSize*iBlankSpace, iFontNormalSize*(iRowIdx+=1));

        iSpaces = (PCPSConfig.PRINT_RECEIPT_PRINTED_COLUMN + PCPSConfig.PRINT_RECEIPT_SHADOW_COLUMN)
                - strParamBuff1.length() - strParamBuff2.length();
        if (iSpaces<=0) {
            iSpaces = 1;
        }

        graph2D.drawString("==========================================", iFontNormalSize*iBlankSpace, iFontNormalSize*(iRowIdx+=2));
        graph2D.drawString("", iFontNormalSize*iBlankSpace, iFontNormalSize*(iRowIdx+=1));

        return new MultipleReturnResult(graph2D, iRowIdx);

    }




    public static MultipleReturnResult settlement(
            ReceiptSettlementData receipt,
            Graphics2D graph2D, int iFontNormalSize,
            int iBlankSpace, Font fontNormalSizePlain, Font fontNormalSizeBold, int iRowIdx) {
        int iSpaces = 0;
        String strParamBuff1 = "";
        String strParamBuff2 = "";
        int iPrintSpace = PCPSConfig.PRINT_RECEIPT_PRINTED_COLUMN;
        int iPrintReceiptWidth = iFontNormalSize*(iBlankSpace+iPrintSpace+iBlankSpace);
        graph2D.setFont(fontNormalSizePlain);

        String tid = receipt.getTid();
        String mid = receipt.getMid();
        String date = receipt.getDate();
        String time = receipt.getTime();
        String batch = receipt.getBatch();
        String host = receipt.getHost();
        String issuer = receipt.getIssuer();
        List<SettlementSummaryReceiptData> summaryList = receipt.getSummaryList();
        strParamBuff1 = "TID   : ";
        strParamBuff2 = "MID : ";
        iSpaces = (PCPSConfig.PRINT_RECEIPT_PRINTED_COLUMN + PCPSConfig.PRINT_RECEIPT_SHADOW_COLUMN)
                - strParamBuff1.length() - tid.length() - strParamBuff2.length() - mid.length();
        if (iSpaces<=0) {
            iSpaces = 1;
        }
        graph2D.drawString(strParamBuff1 + tid + spaces(iSpaces) + strParamBuff2 + mid, iFontNormalSize*iBlankSpace, iFontNormalSize*(iRowIdx+=1));

        strParamBuff1 = "DATE  : ";
        strParamBuff2 = "TIME : ";
        iSpaces = (PCPSConfig.PRINT_RECEIPT_PRINTED_COLUMN + PCPSConfig.PRINT_RECEIPT_SHADOW_COLUMN)
                - strParamBuff1.length() - date.length() - strParamBuff2.length() - time.length();
        if (iSpaces<=0) {
            iSpaces = 1;
        }
        graph2D.drawString(strParamBuff1 + date + spaces(iSpaces) + strParamBuff2 + time, iFontNormalSize*iBlankSpace, iFontNormalSize*(iRowIdx+=1));


        strParamBuff1 = "BATCH : ";
        strParamBuff2 = "HOST : ";
        iSpaces = (PCPSConfig.PRINT_RECEIPT_PRINTED_COLUMN + PCPSConfig.PRINT_RECEIPT_SHADOW_COLUMN)
                - strParamBuff1.length() - batch.length() - strParamBuff2.length() - host.length();
        if (iSpaces<=0) {
            iSpaces = 1;
        }
        graph2D.drawString(strParamBuff1 + batch + spaces(iSpaces) + strParamBuff2 + host, iFontNormalSize*iBlankSpace, iFontNormalSize*(iRowIdx+=1));

        graph2D.drawString("***TRANSACTION TOTALS BY ISSUER***", 100, iFontNormalSize*(iRowIdx+=1));

        strParamBuff1 = "ISSUER: ";
        strParamBuff2 = "HOST : ";
        iSpaces = (PCPSConfig.PRINT_RECEIPT_PRINTED_COLUMN + PCPSConfig.PRINT_RECEIPT_SHADOW_COLUMN)
                - strParamBuff1.length() - issuer.length() - strParamBuff2.length() - host.length();
        if (iSpaces<=0) {
            iSpaces = 1;
        }
        graph2D.drawString(strParamBuff1 + issuer + spaces(iSpaces) + strParamBuff2 + host, iFontNormalSize*iBlankSpace, iFontNormalSize*(iRowIdx+=1));

        for (SettlementSummaryReceiptData data:summaryList){
            String name = data.getKey();
            String count = data.getCount();
            String amount = "Rp. "+data.getAmount();
            int spaceName = 19 - name.length();
            int spaceCount = spaceName + name.length() - count.length();
            int spaceAmount = 23 - amount.length();
            graph2D.drawString(name+ spaces(spaceCount - name.length()) + count + spaces(spaceAmount) + amount, iFontNormalSize*iBlankSpace, iFontNormalSize*(iRowIdx+=1));
        }
        graph2D.drawString("", iFontNormalSize*iBlankSpace, iFontNormalSize*(iRowIdx+=1));
//        graph2D.drawString("GRAND TOTAL", iFontNormalSize*iBlankSpace, iFontNormalSize*(iRowIdx+=1));
//        for (SettlementSummaryReceiptData data:summaryList){
//            String name = data.getKey();
//            String count = data.getCount();
//            String amount = data.getAmount();
//            int spaceName = 19 - name.length();
//            int spaceCount = spaceName + name.length() - count.length();
//            int spaceAmount = 23 - amount.length();
//            graph2D.drawString(name+ spaces(spaceCount - name.length()) + count + spaces(spaceAmount) + amount, iFontNormalSize*iBlankSpace, iFontNormalSize*(iRowIdx+=1));
//        }


        iSpaces = (PCPSConfig.PRINT_RECEIPT_PRINTED_COLUMN + PCPSConfig.PRINT_RECEIPT_SHADOW_COLUMN)
                - strParamBuff1.length() - strParamBuff2.length();
        if (iSpaces<=0) {
            iSpaces = 1;
        }

        return new MultipleReturnResult(graph2D, iRowIdx);
    }

    public static String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s);
    }

    public static String spaces(int spaces) {
        return CharBuffer.allocate(spaces).toString().replace('\0',' ');
    }
}
