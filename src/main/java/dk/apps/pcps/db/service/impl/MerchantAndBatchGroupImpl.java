package dk.apps.pcps.db.service.impl;

import dk.apps.pcps.db.entity.BatchGroup;
import dk.apps.pcps.db.entity.MerchantAndBatchGroup;
import dk.apps.pcps.db.repository.MerchantAndBatchGroupRepository;
import dk.apps.pcps.db.service.MerchantAndBatchGroupService;
import dk.apps.pcps.dbmaster.entity.Merchant;
import dk.apps.pcps.main.handler.ApplicationException;
import dk.apps.pcps.main.model.enums.ProcessMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;




@Component
public class MerchantAndBatchGroupImpl implements MerchantAndBatchGroupService {

    MerchantAndBatchGroupRepository merchantAndBatchGroupRepository;

    @Autowired
    public MerchantAndBatchGroupImpl(MerchantAndBatchGroupRepository merchantAndBatchGroupRepository){
        this.merchantAndBatchGroupRepository = merchantAndBatchGroupRepository;
    }

    @Override
    public String getMid(Merchant merchant, BatchGroup batchGroup) {
        return getMerchantAndBatchGroup(merchant.getId(), batchGroup).getMid();
    }

    @Override
    public MerchantAndBatchGroup getMerchantAndBatchGroup(int merchantId, BatchGroup batchGroup) {
        return merchantAndBatchGroupRepository.findByMerchantIdAndBatchGroup(merchantId, batchGroup)
                .orElseThrow(() -> new ApplicationException(ProcessMessageEnum.MERCHANT_NOT_CONFIGURE_BATCH_GROUP, batchGroup.getName()));
    }
}
