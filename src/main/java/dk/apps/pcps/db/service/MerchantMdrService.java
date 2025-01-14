package dk.apps.pcps.db.service;

import dk.apps.pcps.db.entity.BatchGroup;
import dk.apps.pcps.db.entity.MerchantMdr;


public interface MerchantMdrService {
    MerchantMdr getMerchantMdr(int merchantId, BatchGroup batchGroup);
}
