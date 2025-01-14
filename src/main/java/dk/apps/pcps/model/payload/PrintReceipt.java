package dk.apps.pcps.model.payload;

import lombok.Data;

@Data
public class PrintReceipt {
    private String merchantNameReceipt;
    private String addressLine1; 
    private String addressLine2; 
    private String tid;
    private String mid; 
    private String cardType; 
    private String maskedCAN; 
    private String binResult;
    private String trxType; 
    private String cardExpiryDatePrintReceiptFormat;
    private String batchNum;
    private String invoiceNum;
    private String trxHostDatePrintReceiptFormat;
    private String trxHostTimePrintReceiptFormat;
    private String rrn; 
    private String approvalCode;
    private String baseAmountPrintReceiptFormat;
    private String pinStatus;
    private String bankAcqName;
    private boolean bFirstCopyFlag;
    private String customerSignature;
    private boolean customerSignatureFlag;
    private String bankAcqPrintReceiptLogo;
    private String cardHolderName;
    private String merchantName;
    private long merchantID;
}
