package dk.apps.pcps.main.controller;

import dk.apps.pcps.main.model.payload.ReqPayment;
import dk.apps.pcps.main.model.result.PaymentResult;
import dk.apps.pcps.main.model.result.SuccessPaymentResult;
import dk.apps.pcps.main.model.result.TransactionResult;
import dk.apps.pcps.main.module.PrepaidCardSwitchingService;
import dk.apps.pcps.model.ResponseData;
import dk.apps.pcps.main.model.enums.ProcessMessageEnum;
import dk.apps.pcps.main.model.result.SettlementSummaryResult;
import dk.apps.pcps.main.model.result.fileupload.SettlementFileUploadResult;
import dk.apps.pcps.validation.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/trx")
public class PaymentCtl extends BaseCtl {

    PrepaidCardSwitchingService transactionProcessService;

    @Autowired
    public PaymentCtl(ValidationUtils validationUtils, PrepaidCardSwitchingService transactionProcessService) {
        super(validationUtils);
        this.transactionProcessService = transactionProcessService;
    }

    @PostMapping("/create_payment")
    public ResponseData createPayment( @RequestBody ReqPayment req){
        validationUtils.validate(req);
        TransactionResult transactionResult = transactionProcessService.doSaveTrx(req);
        return new ResponseData()
                .setCode(ProcessMessageEnum.SUCCESS.code)
                .setStatus(ProcessMessageEnum.SUCCESS.name())
                .setMessage(ProcessMessageEnum.SUCCESS.message)
                .setData(transactionResult);
    }

    @GetMapping("/success_payment_list")
    public ResponseData paymentList(){
        SuccessPaymentResult results = transactionProcessService.successPaymentList();
        return new ResponseData()
                .setCode(ProcessMessageEnum.SUCCESS.code)
                .setStatus(ProcessMessageEnum.SUCCESS.name())
                .setMessage(ProcessMessageEnum.SUCCESS.message)
                .setData(results);
    }

    @GetMapping("/history/success-payment-summary")
    public ResponseData paymentSummary(){
        PaymentResult results = transactionProcessService.successPaymentSummary();
        return new ResponseData()
                .setCode(ProcessMessageEnum.SUCCESS.code)
                .setStatus(ProcessMessageEnum.SUCCESS.name())
                .setMessage(ProcessMessageEnum.SUCCESS.message)
                .setData(results);
    }

    @GetMapping("/history/success-payment-detail/{batch_group_id}")
    public ResponseData paymentDetail(@PathVariable("batch_group_id") int batchGroupId){
        PaymentResult results = transactionProcessService.successPaymentDetail(batchGroupId);
        return new ResponseData()
                .setCode(ProcessMessageEnum.SUCCESS.code)
                .setStatus(ProcessMessageEnum.SUCCESS.name())
                .setMessage(ProcessMessageEnum.SUCCESS.message)
                .setData(results);
    }

    @GetMapping("/history/last-payment")
    public ResponseData lastCreatePayment(){
        TransactionResult transactionResult = transactionProcessService.getLastTrx(null);
        return new ResponseData()
                .setCode(ProcessMessageEnum.SUCCESS.code)
                .setStatus(ProcessMessageEnum.SUCCESS.name())
                .setMessage(ProcessMessageEnum.SUCCESS.message)
                .setData(transactionResult);
    }

    @GetMapping("/history/last-payment/{batch_group_id}")
    public ResponseData lastCreatePayment(@PathVariable("batch_group_id") Integer batchGroupId){
        TransactionResult transactionResult = transactionProcessService.getLastTrx(batchGroupId);
        return new ResponseData()
                .setCode(ProcessMessageEnum.SUCCESS.code)
                .setStatus(ProcessMessageEnum.SUCCESS.name())
                .setMessage(ProcessMessageEnum.SUCCESS.message)
                .setData(transactionResult);
    }

    @GetMapping("/last_payment/{batch_group_id}")
    public ResponseData lastCreatePayment( @PathVariable("batch_group_id") int batchGroupId){
        TransactionResult transactionResult = transactionProcessService.getLastTrx(batchGroupId);
        return new ResponseData()
                .setCode(ProcessMessageEnum.SUCCESS.code)
                .setStatus(ProcessMessageEnum.SUCCESS.name())
                .setMessage(ProcessMessageEnum.SUCCESS.message)
                .setData(transactionResult);
    }

//    @PostMapping("/create_refund")
//    public ResponseData createRefund(@RequestHeader("Authorization") String token, @RequestBody ReqRefund req){
//        validationUtils.validate(req);
//        JSONObject jo = Utility.jwtExtractor(token);
//        String username = Utility.getUserEdc(jo);
//        req.setUsername(username);
//        TransactionResult transactionResult = transactionProcessService.doRefund(req);
//        if (transactionResult == null)
//            new ResponseData().setCode("0001").setMessage("payment transaction not found for refund");
//        return new ResponseData()
//                .setCode(ProcessMessageEnum.SUCCESS.code)
//                .setStatus(ProcessMessageEnum.SUCCESS.name())
//                .setMessage(ProcessMessageEnum.SUCCESS.message)
//                .setData(transactionResult);
//    }

    @PostMapping("/settlement/generate")
    public ResponseData settlement(){
        transactionProcessService.generateFileUpload();
        return new ResponseData()
                .setCode(ProcessMessageEnum.SUCCESS.code)
                .setStatus(ProcessMessageEnum.SUCCESS.name())
                .setMessage(ProcessMessageEnum.SUCCESS.message);
    }

    @PostMapping("/settlement/upload")
    public ResponseData settlementUpload(){
        transactionProcessService.uploadSettlementFile();
        return new ResponseData()
                .setCode(ProcessMessageEnum.SUCCESS.code)
                .setStatus(ProcessMessageEnum.SUCCESS.name())
                .setMessage(ProcessMessageEnum.SUCCESS.message);
    }

    @GetMapping("/settlement/check")
    public ResponseData settlementCheck(){
        SettlementFileUploadResult settlementFileUploadResult = transactionProcessService.checkFileUpload();
        return new ResponseData()
                .setCode(ProcessMessageEnum.SUCCESS.code)
                .setStatus(ProcessMessageEnum.SUCCESS.name())
                .setMessage(ProcessMessageEnum.SUCCESS.message)
                .setData(settlementFileUploadResult);
    }

    @PostMapping("/settlement/{batch_group_id}")
    public ResponseData settlement(@PathVariable("batch_group_id") int batchGroupId){
        SettlementSummaryResult settlementResult = transactionProcessService.doSettlement(batchGroupId);
        return new ResponseData()
                .setCode(ProcessMessageEnum.SUCCESS.code)
                .setStatus(ProcessMessageEnum.SUCCESS.name())
                .setMessage(ProcessMessageEnum.SUCCESS.message)
                .setData(settlementResult);
    }
}
