package dk.apps.pcps.main.controller;


import dk.apps.pcps.main.model.enums.ProcessMessageEnum;
import dk.apps.pcps.main.model.payload.ReqRangeHistory;
import dk.apps.pcps.main.model.result.PaymentResult;
import dk.apps.pcps.main.model.result.TrxHistoryResult;

import dk.apps.pcps.main.module.ReportService;
import dk.apps.pcps.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/report")
public class ReportCtl {

    ReportService reportService;

    @Autowired
    public ReportCtl(ReportService reportService) {
        this.reportService = reportService;
    }


    @GetMapping("/settlement/upload/summary/{batch_group_id}")
    public ResponseData getSettlementSummary(@PathVariable("batch_group_id") int batchGroupId){
        PaymentResult results = reportService.settlementSummary(batchGroupId);
        return new ResponseData()
                .setCode(ProcessMessageEnum.SUCCESS.code)
                .setStatus(ProcessMessageEnum.SUCCESS.name())
                .setMessage(ProcessMessageEnum.SUCCESS.message)
                .setData(results);
    }

    @GetMapping("/settlement/history/detail-list/{session_number}")
    public ResponseData settlementHistoryDetail(@PathVariable("session_number") int sessionNumber){
        PaymentResult results = reportService.settlementDetailHistory(sessionNumber);
        return new ResponseData()
                .setCode(ProcessMessageEnum.SUCCESS.code)
                .setStatus(ProcessMessageEnum.SUCCESS.name())
                .setMessage(ProcessMessageEnum.SUCCESS.message)
                .setData(results);
    }

    @PostMapping("/settlement/history/summary-list")
    public ResponseData settlementHistoryList(@RequestBody ReqRangeHistory rangeHistory){
        PaymentResult results = reportService.settlementSummaryHistory(rangeHistory);
        return new ResponseData()
                .setCode(ProcessMessageEnum.SUCCESS.code)
                .setStatus(ProcessMessageEnum.SUCCESS.name())
                .setMessage(ProcessMessageEnum.SUCCESS.message)
                .setData(results);
    }


    @GetMapping("/trx/last_history")
    public ResponseData settlementLastHistory(){
        TrxHistoryResult results = reportService.last10TrxHistory();
        return new ResponseData()
                .setCode(ProcessMessageEnum.SUCCESS.code)
                .setStatus(ProcessMessageEnum.SUCCESS.name())
                .setMessage(ProcessMessageEnum.SUCCESS.message)
                .setData(results);
    }
}
