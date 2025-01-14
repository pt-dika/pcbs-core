package dk.apps.pcps.main.controller;


import dk.apps.pcps.main.module.PrintReceiptService;
import dk.apps.pcps.model.ResponseData;
import dk.apps.pcps.main.model.enums.ProcessMessageEnum;
import dk.apps.pcps.model.payload.ReqPrintReceipt;
import dk.apps.pcps.model.result.PrintReceiptData;
import dk.apps.pcps.model.result.SettlementSummaryResult;
import dk.apps.pcps.model.result.receipt.ReceiptSettlementData;
import dk.apps.pcps.model.result.receipt.ReceiptTemplate;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/v1/receipt")
public class PrintReceiptCtl {

    PrintReceiptService printReceiptService;

    @Autowired
    public PrintReceiptCtl(PrintReceiptService printReceiptService) {
        this.printReceiptService = printReceiptService;
    }

    @PostMapping(value = "/purchase")
    public ResponseData receiptPurchase(@RequestBody ReqPrintReceipt req) throws IOException {
        PrintReceiptData printReceiptData = printReceiptService.saleReceipt(req.getBatchGroupId(), req.getInvoiceNum());
        return new ResponseData()
                .setCode(ProcessMessageEnum.SUCCESS.code)
                .setStatus(ProcessMessageEnum.SUCCESS.name())
                .setMessage(ProcessMessageEnum.SUCCESS.message)
                .setData(printReceiptData);
    }

    @PostMapping(value = "/purchase/data")
    public ResponseData receiptPurchaseData(@RequestBody ReqPrintReceipt req) throws IOException {
        ReceiptTemplate receiptTemplate = printReceiptService.saleReceiptData(req.getBatchGroupId(), req.getInvoiceNum());
        return new ResponseData()
                .setCode(ProcessMessageEnum.SUCCESS.code)
                .setStatus(ProcessMessageEnum.SUCCESS.name())
                .setMessage(ProcessMessageEnum.SUCCESS.message)
                .setData(receiptTemplate);
    }

    @PostMapping(value = "/purchase/email")
    public ResponseData receiptPurchaseEmail(@RequestBody ReqPrintReceipt req) throws IOException {
        return new ResponseData()
                .setCode(ProcessMessageEnum.SUCCESS.code)
                .setStatus(ProcessMessageEnum.SUCCESS.name())
                .setMessage(ProcessMessageEnum.SUCCESS.message);
    }

    @PostMapping(value = "/purchase/result", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] receiptPurchaseResult(@RequestBody ReqPrintReceipt req) throws IOException {
        PrintReceiptData printReceiptData = printReceiptService.saleReceipt(req.getBatchGroupId(), req.getInvoiceNum());
        InputStream in = new ByteArrayInputStream(Base64.decode(printReceiptData.getPrintCustomer().getBytes()));
        return IOUtils.toByteArray(in);
    }

    @PostMapping(value = "/settlement/data")
    public ResponseData receiptSettlementData(@RequestBody ReqPrintReceipt req) throws IOException {
        ReceiptTemplate<ReceiptSettlementData> receiptTemplate = printReceiptService.settlementReceiptData(req.getBatchGroupId(), req.getSessionNum());
        return new ResponseData()
                .setCode(ProcessMessageEnum.SUCCESS.code)
                .setStatus(ProcessMessageEnum.SUCCESS.name())
                .setMessage(ProcessMessageEnum.SUCCESS.message)
                .setData(new SettlementSummaryResult().setSettlementSummary(receiptTemplate.getBody()));
    }

    @PostMapping(value = "/settlement")
    public ResponseData receiptSettlement(@RequestBody ReqPrintReceipt req) throws IOException {
        PrintReceiptData printReceiptData = printReceiptService.settlementReceipt(req.getBatchGroupId(), req.getSessionNum());
        return new ResponseData()
                .setCode(ProcessMessageEnum.SUCCESS.code)
                .setStatus(ProcessMessageEnum.SUCCESS.name())
                .setMessage(ProcessMessageEnum.SUCCESS.message)
                .setData(printReceiptData);
    }

    @GetMapping(value = "/settlement/last/{batch_group_id}")
    public ResponseData receiptLastSettlement(@PathVariable("batch_group_id") Integer batchGroupId) throws IOException {
        PrintReceiptData printReceiptData = printReceiptService.lastSettlementReceipt(batchGroupId);
        return new ResponseData()
                .setCode(ProcessMessageEnum.SUCCESS.code)
                .setStatus(ProcessMessageEnum.SUCCESS.name())
                .setMessage(ProcessMessageEnum.SUCCESS.message)
                .setData(printReceiptData);
    }

    @PostMapping(value = "/settlement/result", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] receiptSettlementResult(@RequestBody ReqPrintReceipt req) throws IOException {
        PrintReceiptData printReceiptData = printReceiptService.settlementReceipt(req.getBatchGroupId(), req.getSessionNum());
        InputStream in = new ByteArrayInputStream(Base64.decode(printReceiptData.getPrintMerchant().getBytes()));
        return IOUtils.toByteArray(in);
    }

    @PostMapping(value = "/settlement/email")
    public ResponseData receiptSettlementEmail(@RequestBody ReqPrintReceipt req) throws IOException {
        return new ResponseData()
                .setCode(ProcessMessageEnum.SUCCESS.code)
                .setStatus(ProcessMessageEnum.SUCCESS.name())
                .setMessage(ProcessMessageEnum.SUCCESS.message);
    }
}
