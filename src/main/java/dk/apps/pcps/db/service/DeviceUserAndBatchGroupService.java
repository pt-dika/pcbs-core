package dk.apps.pcps.db.service;

import dk.apps.pcps.db.entity.BatchGroup;
import dk.apps.pcps.db.entity.DeviceUserAndBatchGroup;

import dk.apps.pcps.model.result.MerchantAndUserData;

public interface DeviceUserAndBatchGroupService {
    MerchantAndUserData getMerchantAndUser(int merchantId, String username, BatchGroup batchGroup);
    Object getAdditionalData(String username, BatchGroup batchGroup);
    String createAdditionalData(String username, BatchGroup batchGroup, String addParam);
    DeviceUserAndBatchGroup getDeviceUserAndBatchGroup(String username, BatchGroup batchGroupId);
    DeviceUserAndBatchGroup updateBatchNumberAndRrn(DeviceUserAndBatchGroup req);
    int getBatchNumber(String username, BatchGroup batchGroup);
    DeviceUserAndBatchGroup updateBatchNumber(String username, BatchGroup batchGroup, int batchNum);
    int getSessionNumber(String username, BatchGroup batchGroup);
    DeviceUserAndBatchGroup updateSessionNumber(String username, BatchGroup batchGroup, int sessNum);
    DeviceUserAndBatchGroup updateRRN(String username, BatchGroup batchGroup, int rrn);
}
