package dk.apps.pcps.db.service;

import dk.apps.pcps.db.entity.MerchantDetail;



public interface MerchantDetailService {

    MerchantDetail create(MerchantDetail merchantDetail);
    MerchantDetail getMerchantDetail(int merchantId);
    String getSettlementDelayTime(int merchantId);
    String updateSettlementDelayTime(int merchantId, String delayTime);
}
