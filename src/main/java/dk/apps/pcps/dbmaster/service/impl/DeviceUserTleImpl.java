package dk.apps.pcps.dbmaster.service.impl;

import dk.apps.pcps.main.model.enums.ProcessMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import dk.apps.pcps.dbmaster.entity.DeviceUserTle;
import dk.apps.pcps.dbmaster.repository.DeviceUserTleRepository;
import dk.apps.pcps.dbmaster.service.DeviceUserTleService;
import dk.apps.pcps.main.handler.ApplicationException;

@Component
public class DeviceUserTleImpl implements DeviceUserTleService {

    DeviceUserTleRepository deviceUserTleRepository;
    @Autowired
    private DeviceUserTleImpl(DeviceUserTleRepository deviceUserTleRepository){
        this.deviceUserTleRepository = deviceUserTleRepository;
    }


    @Override
    public DeviceUserTle getUserTle(String tid, String mid) {
        DeviceUserTle deviceUserTle = deviceUserTleRepository.findByTidAndMid(tid, mid)
                .orElseThrow(() -> new ApplicationException(ProcessMessageEnum.NOT_FOUND));
        return deviceUserTle;
    }
}
