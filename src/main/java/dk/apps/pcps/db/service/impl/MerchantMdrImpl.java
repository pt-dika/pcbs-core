package dk.apps.pcps.db.service.impl;

import dk.apps.pcps.db.entity.BatchGroup;
import dk.apps.pcps.db.entity.MerchantMdr;
import dk.apps.pcps.db.repository.MerchantMdrRepository;
import dk.apps.pcps.db.service.MerchantMdrService;
import dk.apps.pcps.main.handler.ApplicationException;
import dk.apps.pcps.main.model.enums.ProcessMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;




@Component
public class MerchantMdrImpl implements MerchantMdrService {

    MerchantMdrRepository merchantMdrRepository;

    @Autowired
    public MerchantMdrImpl(MerchantMdrRepository merchantMdrRepository){
        this.merchantMdrRepository = merchantMdrRepository;
    }

    @Override
    public MerchantMdr getMerchantMdr(int merchantId, BatchGroup batchGroup) {
        return merchantMdrRepository.findByMerchantIdAndBatchGroup(merchantId, batchGroup)
                .orElseThrow(() -> new ApplicationException(ProcessMessageEnum.MERCHANT_MDR_NOT_CONFIGURED));
    }
}
