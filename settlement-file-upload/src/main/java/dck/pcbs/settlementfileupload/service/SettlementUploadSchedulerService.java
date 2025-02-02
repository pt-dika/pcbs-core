package dck.pcbs.settlementfileupload.service;

import dck.pcbs.cds.entity.MerchantGroupDetail;
import dck.pcbs.cds.repository.MerchantGroupDetailRepository;
import dck.pcbs.settlement.service.SettlementModuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class SettlementUploadSchedulerService {

    @Resource
    MerchantGroupDetailRepository merchantGroupDetailRepository;

    @Resource
    SettlementModuleService settlementModuleService;

    @Scheduled(cron = "${scheduler.cron}", zone = "Asia/Jakarta")
    public void execution() {
        List<MerchantGroupDetail> merchantGroupDetails = merchantGroupDetailRepository.findAllByActiveTrue();
        merchantGroupDetails.forEach(merchantGroupDetail -> settlementModuleService.uploadFileSettlement(merchantGroupDetail.getId()));
    }
}
