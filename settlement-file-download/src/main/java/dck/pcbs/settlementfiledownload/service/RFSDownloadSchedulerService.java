package dck.pcbs.settlementfiledownload.service;

import dck.pcbs.settlement.service.SettlementModuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class RFSDownloadSchedulerService {

    @Resource
    SettlementModuleService settlementModuleService;

    @Scheduled(cron = "${scheduler.cron}", zone = "Asia/Jakarta")
    public void execution(){
        settlementModuleService.downloadFileSettlement();
    }
}
