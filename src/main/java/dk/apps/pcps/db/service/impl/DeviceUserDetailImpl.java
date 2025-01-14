package dk.apps.pcps.db.service.impl;

import dk.apps.pcps.db.entity.DeviceUserDetail;
import dk.apps.pcps.db.repository.DeviceUserDetailRepository;
import dk.apps.pcps.db.service.DeviceUserDetailService;
import dk.apps.pcps.main.handler.ApplicationException;
import dk.apps.pcps.main.model.enums.ProcessMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import dk.apps.pcps.commonutils.Utility;



import dk.apps.pcps.model.payload.AdditionalParam;

@Component
public class DeviceUserDetailImpl implements DeviceUserDetailService {

    DeviceUserDetailRepository deviceUserDetailRepository;

    @Override
    public int getSessionNumber(String username) {
        DeviceUserDetail deviceUserAndBatchGroup = getUserDetail(username);
        int sessNum = deviceUserAndBatchGroup.getSettlementSessionNum() + 1;
        if (sessNum > 999999) {
            sessNum = 1;
        }
        return sessNum;
    }

    @Override
    public DeviceUserDetail updateSessionNumber(String username, int sesNum) {
        DeviceUserDetail data = getUserDetail(username);
        data.setSettlementSessionNum(sesNum);
        data.setLastSettlementAt(Utility.getTimeStamp());
        return deviceUserDetailRepository.save(data);
    }

    @Autowired
    public DeviceUserDetailImpl(DeviceUserDetailRepository deviceUserDetailRepository){
        this.deviceUserDetailRepository = deviceUserDetailRepository;
    }

    @Override
    public DeviceUserDetail getUserDetail(String username) {
        return deviceUserDetailRepository.findByUsername(username)
                .orElseThrow(() -> new ApplicationException(ProcessMessageEnum.USER_NOT_FOUND));
    }

    @Override
    public DeviceUserDetail updateInvoiceNumber(String username, int invoiceNumber) {
        DeviceUserDetail deviceUserDetail = getUserDetail(username);
        deviceUserDetail.setInvoiceNum(invoiceNumber);
        return deviceUserDetailRepository.save(deviceUserDetail);
    }

    @Override
    public DeviceUserDetail saveMarriageCode(String username, String marriageCode) {
        AdditionalParam additionalParam = new AdditionalParam();
        additionalParam.setMarriageCode(marriageCode);
        DeviceUserDetail deviceUserDetail = getUserDetail(username);
        deviceUserDetail.setAdditionalParam(Utility.objectToString(additionalParam));
        return deviceUserDetailRepository.save(deviceUserDetail);
    }
}
