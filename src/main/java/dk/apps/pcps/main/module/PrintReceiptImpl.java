package dk.apps.pcps.main.module;

import dk.apps.pcps.commonutils.ISOConverters;
import dk.apps.pcps.commonutils.Utility;
import dk.apps.pcps.commonutils.print.*;
import dk.apps.pcps.model.result.*;
import dk.apps.pcps.model.result.receipt.*;
import dk.apps.pcps.config.PCPSConfig;
import dk.apps.pcps.config.auth.AuthService;
import dk.apps.pcps.db.entity.BatchGroup;
import dk.apps.pcps.db.entity.Settlement;
import dk.apps.pcps.db.service.CardUpdateHistoryService;
import dk.apps.pcps.db.service.DeviceUserAndBatchGroupService;
import dk.apps.pcps.db.service.SettlementService;
import dk.apps.pcps.db.service.TrxHistoryService;
import dk.apps.pcps.dbmaster.entity.MobileAppUsers;
import dk.apps.pcps.dbmaster.service.BankService;
import dk.apps.pcps.main.handler.ApplicationException;
import dk.apps.pcps.main.model.payload.TrxHistoryData;
import dk.apps.pcps.main.module.tapcash.model.CardData;
import dk.apps.pcps.model.enums.CardEnum;
import dk.apps.pcps.main.model.enums.ProcessMessageEnum;
import dk.apps.pcps.model.payload.PrintReceipt;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static dk.apps.pcps.commonutils.print.PrintReceiptFooterVoid.drawCenteredString;

@Service
public class PrintReceiptImpl implements PrintReceiptService {

    int iFontNormalSize = 0;
    int iBlankSpace = 0;
    int iPrintSpace = 0;
    int iPrintReceiptWidth = 0;
    int iPrintReceiptHeight = 0; // max row

    Font fontNormalSizePlain;
    Font fontNormalSizeBold;
    Font fontNormalSizeItalic;

    AuthService authService;
    TrxHistoryService trxHistoryService;
    SettlementService settlementService;
    DeviceUserAndBatchGroupService deviceUserAndBatchGroupService;
    BankService bankService;
    CardUpdateHistoryService cardUpdateHistoryService;

    @Autowired
    public PrintReceiptImpl(AuthService authService, TrxHistoryService trxHistoryService, SettlementService settlementService, DeviceUserAndBatchGroupService deviceUserAndBatchGroupService, BankService bankService, CardUpdateHistoryService cardUpdateHistoryService){
        this.iFontNormalSize = PCPSConfig.PRINT_RECEIPT_FONT_NORMAL_SIZE;
        this.iBlankSpace = PCPSConfig.PRINT_RECEIPT_BLANK_COLUMN;
        this.iPrintSpace = PCPSConfig.PRINT_RECEIPT_PRINTED_COLUMN;
        this.iPrintReceiptWidth = iFontNormalSize*(iBlankSpace+iPrintSpace+iBlankSpace);
        this.iPrintReceiptHeight = iFontNormalSize* PCPSConfig.PRINT_RECEIPT_TRX_ROW; // max row

        this.fontNormalSizePlain = new Font(PCPSConfig.PRINT_RECEIPT_FONT, Font.PLAIN, iFontNormalSize);
        this.fontNormalSizeBold = new Font(PCPSConfig.PRINT_RECEIPT_FONT, Font.BOLD, iFontNormalSize);
        this.fontNormalSizeItalic = new Font(PCPSConfig.PRINT_RECEIPT_FONT, Font.ITALIC, iFontNormalSize);

        this.authService = authService;
        this.trxHistoryService = trxHistoryService;
        this.settlementService = settlementService;
        this.deviceUserAndBatchGroupService = deviceUserAndBatchGroupService;
        this.bankService = bankService;
        this.cardUpdateHistoryService = cardUpdateHistoryService;
    }

    private BufferedImage initBufferedImage(){
        return new BufferedImage(iPrintReceiptWidth, iPrintReceiptHeight, BufferedImage.TYPE_INT_ARGB);
    }

    private Graphics2D initGraphics2D(BufferedImage bufImg){
        Graphics2D graph2D = bufImg.createGraphics();
        graph2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graph2D.setColor(Color.WHITE);
        graph2D.fillRect(0, 0, iPrintReceiptWidth, iPrintReceiptHeight);
        graph2D.setColor(Color.BLACK);
        return graph2D;
    }

    @Override
    public PrintReceiptData saleReceipt(int batchGroupId, int invoiceNum) {
        ReceiptTemplate<ReceiptPurchaseData> receiptTemplate = saleReceiptData(batchGroupId, invoiceNum);
        ReceiptHeader header = receiptTemplate.getHeader();
        ReceiptPurchaseData body = receiptTemplate.getBody();
        ReceiptFooter footer = receiptTemplate.getFooter();

        PrintReceiptData printReceiptData = new PrintReceiptData();

        try{
            BufferedImage bufImg = initBufferedImage();
            Graphics2D graph2D = initGraphics2D(bufImg);
            graph2D = PrintReceiptLogo.printLogo(graph2D, iPrintReceiptWidth, iFontNormalSize, header.getLogo());

            int iRowIdx = 6;
            MultipleReturnResult multipleReturnResult = PrintReceiptHeader.printHeader(
                    graph2D,
                    header.getMerchantName(),
                    header.getAddress(),
                    header.getAddress2(),
                    header.getMerchantName(),
                    header.getMerchantId(),
                    iPrintReceiptWidth, iFontNormalSize,
                    fontNormalSizePlain, iRowIdx);
            iRowIdx = multipleReturnResult.getRowIdx();
            graph2D = multipleReturnResult.getGraph2D();

            multipleReturnResult = PrintReceiptBody.purchase(
                    body,
                    graph2D, iFontNormalSize,
                    iBlankSpace, fontNormalSizePlain, fontNormalSizeBold, iRowIdx);
            iRowIdx = multipleReturnResult.getRowIdx();
            graph2D = multipleReturnResult.getGraph2D();

            multipleReturnResult = PrintReceiptFooter.printFooter(
                    graph2D, "1", header.getBankCode(), iPrintReceiptWidth,
                    iFontNormalSize, fontNormalSizePlain, fontNormalSizeItalic, iRowIdx,
                    iBlankSpace, "", false);
            iRowIdx = multipleReturnResult.getRowIdx();
            graph2D = multipleReturnResult.getGraph2D();

            String strrintReceiptCustomer = "";
            String strPrintReceiptMerchant = "";
            String strPrintReceiptDuplicate = "";
            if (header.isCopy()==true) {
                drawCenteredString(graph2D, "--CUSTOMER COPY--", iPrintReceiptWidth, iFontNormalSize*(iRowIdx+=2), fontNormalSizePlain);
                strrintReceiptCustomer = Utility.encodeToString(bufImg,"png");

                graph2D.setColor(Color.WHITE);
                graph2D.fillRect(0, iFontNormalSize*(iRowIdx-1), iPrintReceiptWidth, iFontNormalSize);
                graph2D.setColor(Color.BLACK);
                drawCenteredString(graph2D, "--MERCHANT COPY--", iPrintReceiptWidth, iFontNormalSize*(iRowIdx), fontNormalSizePlain);
                strPrintReceiptMerchant = Utility.encodeToString(bufImg,"png");
            } else {
                drawCenteredString(graph2D, "--DUPLICATE COPY--", iPrintReceiptWidth, iFontNormalSize*(iRowIdx+=2), fontNormalSizePlain);
                strPrintReceiptDuplicate = Utility.encodeToString(bufImg,"png");
            }

            graph2D.dispose();

            if (header.isCopy() == true) {
                printReceiptData.setPrintCustomer(strrintReceiptCustomer);
                printReceiptData.setPrintMerchant(strPrintReceiptMerchant);
            } else {
                printReceiptData.setPrintCopy(strPrintReceiptDuplicate);
            }
        } catch (Exception e){

        }
        return printReceiptData;
    }

    @Override
    public ReceiptTemplate saleReceiptData(int batchGroupId, int invoiceNum) {
        MobileAppUsers appUsers = authService.getAuthUsers();
        int merchantId = appUsers.getMerchant().getId();
        long lastBalance = 0;
        long currentBalance = 0;
        String merchantName = appUsers.getMerchant().getName();
        String username = appUsers.getUsername();
        String addr1 = appUsers.getPrintReceiptAddressLine1();
        String addr2 = appUsers.getPrintReceiptAddressLine2();
        BatchGroup batchGroup = new BatchGroup();
        batchGroup.setId(batchGroupId);
        TrxHistoryData trxHistoryData = trxHistoryService.getTrxHistory(username, batchGroup, invoiceNum);
        CardEnum card = CardEnum.getCardEnum(batchGroupId);
        CardData cardData = null;
        switch (card){
            case TAPCASH:
                cardData = Utility.jsonToObject(trxHistoryData.getAdditionalData(), CardData.class);
                lastBalance = Long.parseLong(cardData.getPurseBalance(), 16);
                currentBalance = Long.parseLong(cardData.getLastPurseBalance(), 16);
                break;
            default:
                break;
        }
        LocalDateTime createAtDate = trxHistoryData.getCreateAt().toLocalDateTime();
        MerchantAndUserData merchantAndUserData = deviceUserAndBatchGroupService.getMerchantAndUser(merchantId, username, batchGroup);
        BankData bankData = bankService.getBankData(merchantAndUserData.getBankId());
        ReceiptHeader header = new ReceiptHeader();
        header.setLogo(bankData.getPrintReceiptLogo().split(",")[1]);
        header.setBankCode(bankData.getCode());
        header.setMerchantId(merchantId);
        header.setMerchantName(merchantName);
        header.setAddress(addr1);
        header.setAddress2(addr2);
        header.setCopy(true);

        ReceiptPurchaseData body = new ReceiptPurchaseData();
        body.setTid(trxHistoryData.getTid());
        body.setMid(trxHistoryData.getMid());
        body.setTrxType(trxHistoryData.getTrxType());
        body.setBatch(ISOConverters.padLeftZeros(""+trxHistoryData.getBatchNum(), 6));
        body.setInvoiceNo(trxHistoryData.getInvoiceNum());
        body.setDate(Utility.dateTimeFormat("dd MMM yyyy", createAtDate));
        body.setTime(Utility.dateTimeFormat("hh:mm:ss", createAtDate));
        body.setPtc("");
        body.setApprovalCode("");
        body.setCertData("");
        body.setCounterData(cardData.getCounterData());
        body.setTransHeader(cardData.getTransHeader().toUpperCase());
        body.setSignCert(cardData.getSignCert());
        body.setMaskedCan(trxHistoryData.getMaskedCan());
        body.setPrevBal("Rp "+ NumberFormat.getNumberInstance(Locale.US).format(lastBalance));
        body.setAmount("Rp "+ NumberFormat.getNumberInstance(Locale.US).format(trxHistoryData.getBaseAmount()));
        body.setCurrBal("Rp "+ NumberFormat.getNumberInstance(Locale.US).format(currentBalance));

        ReceiptFooter footer = new ReceiptFooter();
        return new ReceiptTemplate().setHeader(header).setBody(body).setFooter(footer);
    }

    @Override
    public PrintReceiptData settlementReceipt(int batchGroupId, int sessionNum) throws IOException {
        ReceiptTemplate<ReceiptSettlementData> receiptTemplate = settlementReceiptData(batchGroupId, sessionNum);
        ReceiptHeader header = receiptTemplate.getHeader();
        ReceiptSettlementData body = receiptTemplate.getBody();
        ReceiptFooter footer = receiptTemplate.getFooter();

        BufferedImage bufImg = initBufferedImage();
        Graphics2D graph2D = initGraphics2D(bufImg);
        graph2D = PrintReceiptLogo.printLogo(graph2D, iPrintReceiptWidth, iFontNormalSize, header.getLogo());

        int iRowIdx = 6;
        MultipleReturnResult multipleReturnResult = PrintReceiptHeader.printHeader(
                graph2D,
                header.getMerchantName(),
                header.getAddress(),
                header.getAddress2(),
                header.getMerchantName(),
                header.getMerchantId(),
                iPrintReceiptWidth, iFontNormalSize,
                fontNormalSizePlain, iRowIdx);
        iRowIdx = multipleReturnResult.getRowIdx();
        graph2D = multipleReturnResult.getGraph2D();

        multipleReturnResult = PrintReceiptBody.settlement(
                body,
                graph2D, iFontNormalSize,
                iBlankSpace, fontNormalSizePlain, fontNormalSizeBold, iRowIdx);
        iRowIdx = multipleReturnResult.getRowIdx();
        graph2D = multipleReturnResult.getGraph2D();

        multipleReturnResult = PrintReceiptFooter.printFooter(
                graph2D, "2", header.getBankCode(), iPrintReceiptWidth,
                iFontNormalSize, fontNormalSizePlain, fontNormalSizeItalic, iRowIdx,
                iBlankSpace, "", false);
        iRowIdx = multipleReturnResult.getRowIdx();
        graph2D = multipleReturnResult.getGraph2D();

        String strrintReceiptCustomer = "";
        String strPrintReceiptMerchant = "";
        String strPrintReceiptDuplicate = "";
        if (header.isCopy()==true) {
            graph2D.setColor(Color.WHITE);
            graph2D.fillRect(0, iFontNormalSize*(iRowIdx-1), iPrintReceiptWidth, iFontNormalSize);
            graph2D.setColor(Color.BLACK);
            drawCenteredString(graph2D, "--MERCHANT COPY--", iPrintReceiptWidth, iFontNormalSize*(iRowIdx), fontNormalSizePlain);
            strPrintReceiptMerchant = Utility.encodeToString(bufImg,"png");
        } else {
            drawCenteredString(graph2D, "--DUPLICATE COPY--", iPrintReceiptWidth, iFontNormalSize*(iRowIdx+=2), fontNormalSizePlain);
            strPrintReceiptDuplicate = Utility.encodeToString(bufImg,"png");
        }

        graph2D.dispose();

        System.out.println("create return value...");
        PrintReceiptData printReceiptData = new PrintReceiptData();
        if (header.isCopy() == true) {
            printReceiptData.setPrintMerchant(strPrintReceiptMerchant);
        } else {
            printReceiptData.setPrintCopy(strPrintReceiptDuplicate);
        }
        return printReceiptData;
    }

    @Override
    public PrintReceiptData lastSettlementReceipt(int batchGroupId) throws IOException {
        MobileAppUsers appUsers = authService.getAuthUsers();
        String username = appUsers.getUsername();
        Settlement settlement = settlementService.getLastSettlement(username, batchGroupId);
        return settlementReceipt(batchGroupId, settlement.getSettlementSessionNum());
    }

    @Override
    public ReceiptTemplate settlementReceiptData(int batchGroupId, int sessionNum) throws IOException {
        MobileAppUsers appUsers = authService.getAuthUsers();
        int merchantId = appUsers.getMerchant().getId();
        String merchantName = appUsers.getMerchant().getName();
        String username = appUsers.getUsername();
        String addr1 = appUsers.getPrintReceiptAddressLine1();
        String addr2 = appUsers.getPrintReceiptAddressLine2();
        BatchGroup batchGroup = new BatchGroup();
        batchGroup.setId(batchGroupId);
        MerchantAndUserData merchantAndUserData = deviceUserAndBatchGroupService.getMerchantAndUser(merchantId, username, batchGroup);
        BankData bankData = bankService.getBankData(merchantAndUserData.getBankId());
        Settlement settlement = settlementService.getSettlementData(batchGroupId, sessionNum);
        if (settlement == null)
            throw new ApplicationException(ProcessMessageEnum.NOT_FOUND);
        long totalBaseAmount = settlement.getSuccessPaymentTotalBaseAmount();
        int trxCount = settlement.getNumOfSuccessPayment();
        String updBlcCount = "000";
        long updBlcTotal = 0;
        CardUpdateHistoryData cardUpdateHistoryData = cardUpdateHistoryService.getCardUpdateHistory(username, sessionNum);
        if (cardUpdateHistoryData != null){
            updBlcCount = ISOConverters.padLeftZeros(""+cardUpdateHistoryData.getCount(), 3);
            updBlcTotal = cardUpdateHistoryData.getTotalAmount();
        }
        LocalDateTime createAtDate = LocalDateTime.now();
        ReceiptHeader header = new ReceiptHeader();
        header.setLogo(bankData.getPrintReceiptLogo().split(",")[1]);
        header.setBankCode(bankData.getCode());
        header.setMerchantId(merchantId);
        header.setMerchantName(merchantName);
        header.setAddress(addr1);
        header.setAddress2(addr2);
        header.setCopy(true);

        ReceiptSettlementData body = new ReceiptSettlementData();
        body.setBatch(ISOConverters.padLeftZeros(""+settlement.getBatchNum(), 6));
        body.setTid(merchantAndUserData.getTid());
        body.setMid(merchantAndUserData.getMid());
        body.setDate(Utility.dateTimeFormat("dd MMM yyyy", createAtDate));
        body.setTime(Utility.dateTimeFormat("hh:mm:ss", createAtDate));
        body.setHost(settlement.getTrxChannel().getNii());
        body.setIssuer("PREPAID");
        List<SettlementSummaryReceiptData> dataList = Arrays.asList(
                new SettlementSummaryReceiptData().setKey("SALE").setCount(ISOConverters.padLeftZeros(""+trxCount, 3)).setAmount(totalBaseAmount),
                new SettlementSummaryReceiptData().setKey("STATE FEE").setCount("000").setAmount(0),
                new SettlementSummaryReceiptData().setKey("GRACE FEE").setCount("000").setAmount(0),
                new SettlementSummaryReceiptData().setKey("UPDATE BLC").setCount(updBlcCount).setAmount(updBlcTotal),
                new SettlementSummaryReceiptData().setKey("REFUND CASH").setCount("000").setAmount(0),
                new SettlementSummaryReceiptData().setKey("LOY REDEEM").setCount("000").setAmount(0),
                new SettlementSummaryReceiptData().setKey("ACTIVATION").setCount("000").setAmount(0),
                new SettlementSummaryReceiptData().setKey("ATU ACTIVE").setCount("000").setAmount(0),
                new SettlementSummaryReceiptData().setKey("ATU DEREGIS").setCount("000").setAmount(0)
        );
        body.setSummaryList(dataList);
        ReceiptFooter footer = new ReceiptFooter();
        return new ReceiptTemplate().setHeader(header).setBody(body).setFooter(footer);
    }

    @Override
    public PrintReceiptData saleReceipt(PrintReceipt printReceipt) throws IOException {
        return null;
    }

    @Override
    public PrintReceiptData settlementReceipt(PrintReceipt printReceipt) {
        return null;
    }
}
