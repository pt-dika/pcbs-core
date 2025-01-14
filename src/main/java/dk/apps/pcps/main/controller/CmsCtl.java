package dk.apps.pcps.main.controller;

import dk.apps.pcps.config.mqtt.IMqttService;
import dk.apps.pcps.db.entity.BatchGroup;
import dk.apps.pcps.db.service.BatchGroupService;
import dk.apps.pcps.db.service.DeviceUserAndBatchGroupService;
import dk.apps.pcps.db.service.MerchantAndBatchGroupService;

import dk.apps.pcps.dbmaster.service.MobileAppUsersService;
import dk.apps.pcps.main.handler.ApplicationException;
import dk.apps.pcps.main.model.enums.ProcessMessageEnum;
import dk.apps.pcps.main.module.tapcash.model.LogonData;
import dk.apps.pcps.main.module.tapcash.model.MqttJob;
import dk.apps.pcps.main.module.tapcash.model.RespSamStatus;
import dk.apps.pcps.main.module.tapcash.model.SamData;
import dk.apps.pcps.main.utils.Utility;
import dk.apps.pcps.model.ResponseData;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cms")
public class CmsCtl {

    BatchGroupService batchGroupService;
    MobileAppUsersService mobileAppUsersService;
    DeviceUserAndBatchGroupService deviceUserAndBatchGroupService;
    MerchantAndBatchGroupService merchantAndBatchGroupService;
    IMqttService mqttService;

    @Autowired
    public CmsCtl(BatchGroupService batchGroupService,
                  MobileAppUsersService mobileAppUsersService,
                  DeviceUserAndBatchGroupService deviceUserAndBatchGroupService,
                  MerchantAndBatchGroupService merchantAndBatchGroupService,
                  IMqttService mqttService
    ) {
        this.batchGroupService = batchGroupService;
        this.mobileAppUsersService = mobileAppUsersService;
        this.deviceUserAndBatchGroupService = deviceUserAndBatchGroupService;
        this.merchantAndBatchGroupService = merchantAndBatchGroupService;
        this.mqttService = mqttService;
    }

    @GetMapping(path = "/tap-cash/get-sam-status/{user_device}")
    public ResponseData getSamStatus(@PathVariable("user_device") String userDevice) {
        BatchGroup batchGroup = batchGroupService.getBatchGroup(1);
        LogonData logonData = Utility.mapperToDto(deviceUserAndBatchGroupService.getAdditionalData(userDevice, batchGroup), LogonData.class);
        SamData samData = logonData.getSamData();
        return new ResponseData()
                .setCode(ProcessMessageEnum.SUCCESS.code)
                .setStatus(ProcessMessageEnum.SUCCESS.name())
                .setMessage(ProcessMessageEnum.SUCCESS.message)
                .setData(new RespSamStatus().setSamMarry(samData != null).setMarriageCode(samData != null?samData.getPairCode():null));
    }

    @PostMapping(path = "/tap-cash/publish/sam-init/{user_device}")
    public ResponseData publishSamMarry(@PathVariable("user_device") String userDevice) {
        MqttJob job = new MqttJob();
        job.setJob("tap_cash-sam-init");
        try {
            mqttService.publish("tj_user/"+userDevice,Utility.objectToString(job));
        } catch (MqttException e){
            throw new ApplicationException(ProcessMessageEnum.FAILED, e.getMessage());
        }
        return new ResponseData()
                .setCode(ProcessMessageEnum.SUCCESS.code)
                .setStatus(ProcessMessageEnum.SUCCESS.name())
                .setMessage(ProcessMessageEnum.SUCCESS.message);
    }

    @PostMapping(path = "/tap-cash/publish/sam-un-marry/{user_device}")
    public ResponseData publishSamUnMarry(@PathVariable("user_device") String userDevice) {
        MqttJob job = new MqttJob();
        job.setJob("tap_cash-sam-un-marry");
        try {
            mqttService.publish("tj_user/"+userDevice, Utility.objectToString(job));
        } catch (MqttException e){
            throw new ApplicationException(ProcessMessageEnum.FAILED, e.getMessage());
        }
        return new ResponseData()
                .setCode(ProcessMessageEnum.SUCCESS.code)
                .setStatus(ProcessMessageEnum.SUCCESS.name())
                .setMessage(ProcessMessageEnum.SUCCESS.message);
    }
}
