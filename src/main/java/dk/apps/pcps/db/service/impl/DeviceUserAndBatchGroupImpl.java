package dk.apps.pcps.db.service.impl;

import dk.apps.pcps.commonutils.Utility;
import dk.apps.pcps.config.auth.AuthService;
import dk.apps.pcps.db.entity.BatchGroup;
import dk.apps.pcps.db.entity.DeviceUserAndBatchGroup;
import dk.apps.pcps.db.entity.MerchantAndBatchGroup;
import dk.apps.pcps.db.repository.DeviceUserAndBatchGroupRepository;
import dk.apps.pcps.db.service.DeviceUserAndBatchGroupService;
import dk.apps.pcps.db.service.MerchantAndBatchGroupService;
import dk.apps.pcps.main.handler.ApplicationException;
import dk.apps.pcps.model.enums.CardEnum;
import dk.apps.pcps.main.model.enums.ProcessMessageEnum;
import dk.apps.pcps.model.iso8583.bni.model.LogonData;
import dk.apps.pcps.model.result.MerchantAndUserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;




@Component
public class DeviceUserAndBatchGroupImpl implements DeviceUserAndBatchGroupService {

    AuthService authService;
    DeviceUserAndBatchGroupRepository deviceUserAndBatchGroupRepository;
    MerchantAndBatchGroupService merchantAndBatchGroupService;

    @Autowired
    public DeviceUserAndBatchGroupImpl(AuthService authService, DeviceUserAndBatchGroupRepository deviceUserAndBatchGroupRepository, MerchantAndBatchGroupService merchantAndBatchGroupService){
        this.authService = authService;
        this.deviceUserAndBatchGroupRepository = deviceUserAndBatchGroupRepository;
        this.merchantAndBatchGroupService = merchantAndBatchGroupService;
    }

    @Override
    public MerchantAndUserData getMerchantAndUser(int merchantId, String username, BatchGroup batchGroup) {
        DeviceUserAndBatchGroup deviceUserAndBatchGroup = getDeviceUserAndBatchGroup(username, batchGroup);
        MerchantAndBatchGroup merchantAndBatchGroup = merchantAndBatchGroupService.getMerchantAndBatchGroup(merchantId, batchGroup);
        String tid = deviceUserAndBatchGroup.getTid();
        String mid = merchantAndBatchGroup.getMid();
        int bankId = merchantAndBatchGroup.getBatchGroup().getBankId();
        return new MerchantAndUserData().setUsername(username).setMerchantId(merchantId).setTid(tid).setMid(mid).setBankId(bankId);
    }

    @Override
    public Object getAdditionalData(String username, BatchGroup batchGroup) {
        Object object = null;
        CardEnum card = CardEnum.getCardEnum(batchGroup.getId());
        DeviceUserAndBatchGroup data = getDeviceUserAndBatchGroup(username, batchGroup);
        if (!data.getAdditionalParam().equalsIgnoreCase("")){
            switch (card){
                case TAPCASH:
                    object = Utility.jsonToObject(data.getAdditionalParam(), LogonData.class);
                    break;

            }
        }
        return object;
    }

    @Override
    public String createAdditionalData(String username, BatchGroup batchGroup, String addParam) {
        DeviceUserAndBatchGroup data = getDeviceUserAndBatchGroup(username, batchGroup);
        if (!data.getAdditionalParam().equalsIgnoreCase("")){
            data.setAdditionalParam(addParam);
            data = deviceUserAndBatchGroupRepository.save(data);
        }
        return data.getAdditionalParam();
    }

    @Override
    public DeviceUserAndBatchGroup getDeviceUserAndBatchGroup(String username, BatchGroup batchGroup) {
        return deviceUserAndBatchGroupRepository.findByUsernameAndBatchGroup(username, batchGroup)
                .orElseThrow(() -> new ApplicationException(ProcessMessageEnum.USER_NOT_CONFIGURE_BATCH_GROUP));
    }

    @Override
    public DeviceUserAndBatchGroup updateBatchNumberAndRrn(DeviceUserAndBatchGroup req) {
        DeviceUserAndBatchGroup data = getDeviceUserAndBatchGroup(req.getUsername(), req.getBatchGroup());
        data.setBatchNum(req.getBatchNum());
        data.setRrn(req.getRrn());
        return deviceUserAndBatchGroupRepository.save(data);
    }

    @Override
    public int getBatchNumber(String username, BatchGroup batchGroup) {
        DeviceUserAndBatchGroup deviceUserAndBatchGroup = getDeviceUserAndBatchGroup(username, batchGroup);
        int batchNum = deviceUserAndBatchGroup.getBatchNum() + 1;
        if (batchNum > 999999) {
            batchNum = 1;
        }
        return batchNum;
    }

    @Override
    public DeviceUserAndBatchGroup updateBatchNumber(String username, BatchGroup batchGroup, int batchNum) {
        DeviceUserAndBatchGroup data = getDeviceUserAndBatchGroup(username, batchGroup);
        data.setBatchNum(batchNum);
        return deviceUserAndBatchGroupRepository.save(data);
    }

    @Override
    public int getSessionNumber(String username, BatchGroup batchGroup) {
        DeviceUserAndBatchGroup deviceUserAndBatchGroup = getDeviceUserAndBatchGroup(username, batchGroup);
        int sessNum = deviceUserAndBatchGroup.getSessionNum() + 1;
        if (sessNum > 999999) {
            sessNum = 1;
        }
        return sessNum;
    }

    @Override
    public DeviceUserAndBatchGroup updateSessionNumber(String username, BatchGroup batchGroup, int sesNum) {
        DeviceUserAndBatchGroup data = getDeviceUserAndBatchGroup(username, batchGroup);
        data.setSessionNum(sesNum);
        data.setLastSettlementAt(Utility.getTimeStamp());
        return deviceUserAndBatchGroupRepository.save(data);
    }

    @Override
    public DeviceUserAndBatchGroup updateRRN(String username, BatchGroup batchGroup, int rrn) {
        DeviceUserAndBatchGroup data = getDeviceUserAndBatchGroup(username, batchGroup);
        data.setRrn(rrn);
        return deviceUserAndBatchGroupRepository.save(data);
    }
}
