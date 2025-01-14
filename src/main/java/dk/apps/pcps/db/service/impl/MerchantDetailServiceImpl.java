package dk.apps.pcps.db.service.impl;

import dk.apps.pcps.commonutils.Utility;
import dk.apps.pcps.db.entity.MerchantDetail;
import dk.apps.pcps.db.repository.MerchantDetailRepository;
import dk.apps.pcps.db.service.MerchantDetailService;
import dk.apps.pcps.main.model.enums.ProcessMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import dk.apps.pcps.main.handler.ApplicationException;

import java.sql.Time;

@Service
public class MerchantDetailServiceImpl implements MerchantDetailService {

    MerchantDetailRepository merchantDetailRepository;

    @Autowired
    public MerchantDetailServiceImpl(MerchantDetailRepository merchantDetailRepository){
        this.merchantDetailRepository = merchantDetailRepository;
    }

    @Override
    public MerchantDetail create(MerchantDetail merchantDetail) {
        return merchantDetailRepository.save(merchantDetail);
    }

    @Override
    public MerchantDetail getMerchantDetail(int merchantId) {
        return merchantDetailRepository.findByMerchantId(merchantId)
                .orElseThrow(() -> new ApplicationException(ProcessMessageEnum.NOT_FOUND, "merchant not configure this action"));
    }

    @Override
    public String getSettlementDelayTime(int merchantId) {
        MerchantDetail md = getMerchantDetail(merchantId);
        return Utility.timeToStr(md.getSettlementDelayTime());
    }

    @Override
    public String updateSettlementDelayTime(int merchantId, String delayTime) {
        Time time = Utility.strToTime(delayTime);
        MerchantDetail md = getMerchantDetail(merchantId);
        md.setSettlementDelayTime(time);
        return create(md).getSettlementDelayTime().toString();
    }
}
