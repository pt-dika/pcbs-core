package dk.apps.pcps.db.service;

import dk.apps.pcps.db.entity.BatchGroup;
import dk.apps.pcps.db.entity.MerchantAndBatchGroup;

import dk.apps.pcps.dbmaster.entity.Merchant;

public interface MerchantAndBatchGroupService {
    String getMid(Merchant merchant, BatchGroup batchGroup);
    MerchantAndBatchGroup getMerchantAndBatchGroup(int merchantId, BatchGroup batchGroup);
}
