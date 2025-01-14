package dk.apps.pcps.main.controller;

import dk.apps.pcps.main.model.payload.*;

import dk.apps.pcps.main.model.result.BankInfo;
import dk.apps.pcps.main.module.PrepaidCardSwitchingService;
import dk.apps.pcps.main.module.tapcash.TapCashService;
import dk.apps.pcps.main.module.tapcash.model.LogonData;
import dk.apps.pcps.main.module.tapcash.model.SamData;
import dk.apps.pcps.model.ResponseData;
import dk.apps.pcps.main.model.enums.ProcessMessageEnum;
import dk.apps.pcps.model.payload.ReqSaveTmk;
import dk.apps.pcps.main.model.result.BinAllowResult;
import dk.apps.pcps.validation.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/config")
public class ConfigCtl extends BaseCtl{

    PrepaidCardSwitchingService prepaidCardSwitchingService;
    TapCashService tapCashService;

    @Autowired
    public ConfigCtl(ValidationUtils validationUtils, PrepaidCardSwitchingService prepaidCardSwitchingService, TapCashService tapCashService) {
        super(validationUtils);
        this.prepaidCardSwitchingService = prepaidCardSwitchingService;
        this.tapCashService = tapCashService;
    }

    @PostMapping(value = "/create_tmk")
    public ResponseData createTmk(@RequestBody ReqSaveTmk req){
        tapCashService.saveTMK(req.getBatchGroupId(), req.getSeed(), req.getTmk(), req.getMtmkIndex());
        return new ResponseData()
                .setCode(ProcessMessageEnum.SUCCESS.code)
                .setStatus(ProcessMessageEnum.SUCCESS.name())
                .setMessage(ProcessMessageEnum.SUCCESS.message);
    }

    @PostMapping(value = "/logon")
    public ResponseData logonProcess(@RequestBody ReqLogon req){
        LogonData logonData = tapCashService.logonProcess(req, false);
        return new ResponseData()
                .setCode(ProcessMessageEnum.SUCCESS.code)
                .setStatus(ProcessMessageEnum.SUCCESS.name())
                .setMessage(ProcessMessageEnum.SUCCESS.message)
                .setData(logonData);
    }

    @PostMapping(value = "/logoff")
    public ResponseData logoffProcess(@RequestBody ReqLogon req){
        tapCashService.logoff(req);
        return new ResponseData()
                .setCode(ProcessMessageEnum.SUCCESS.code)
                .setStatus(ProcessMessageEnum.SUCCESS.name())
                .setMessage(ProcessMessageEnum.SUCCESS.message);
    }

    @PostMapping(value = "/tap-cash/sam/create-data")
    public ResponseData createSamData(@RequestBody ReqPairingSamCard req){
        validationUtils.validate(req);
        SamData samData = tapCashService.createOrUpdateMarryCode(req.getPairCode());
        return new ResponseData()
                .setCode(ProcessMessageEnum.SUCCESS.code)
                .setStatus(ProcessMessageEnum.SUCCESS.name())
                .setMessage(ProcessMessageEnum.SUCCESS.message)
                .setData(samData);
    }

    @PostMapping(value = "/tap-cash/sam/remove-data")
    public ResponseData removeSamData(@RequestBody ReqUnPairUpdate req){
        int batchGroupId = req.getBatchGroupId();
        tapCashService.unMarrySuccess(req.getUnPairCode(), req.getExtAuth());
        return new ResponseData()
                .setCode(ProcessMessageEnum.SUCCESS.code)
                .setStatus(ProcessMessageEnum.SUCCESS.name())
                .setMessage(ProcessMessageEnum.SUCCESS.message);
    }

    @PostMapping(value = "/tap-cash/sam/un-marry-code")
    public ResponseData getUnMarryCode(@RequestBody ReqUnPair req){
        validationUtils.validate(req);
        RespUnPair respUnPair = tapCashService.generateUnMarryCode(req.getSamId(), req.getCrn(), req.getTrn());
        return new ResponseData()
                .setCode(ProcessMessageEnum.SUCCESS.code)
                .setStatus(ProcessMessageEnum.SUCCESS.name())
                .setMessage(ProcessMessageEnum.SUCCESS.message)
                .setData(respUnPair);
    }

    @GetMapping(value = "/bin/aid_list")
    public ResponseData getBinAllowed(){
        BinAllowResult binAllow = prepaidCardSwitchingService.getAidBinList();
        return new ResponseData()
                .setCode(ProcessMessageEnum.SUCCESS.code)
                .setStatus(ProcessMessageEnum.SUCCESS.name())
                .setMessage(ProcessMessageEnum.SUCCESS.message)
                .setData(binAllow);
    }

    @GetMapping(value = "/bank-info/{batch_group_id}")
    public ResponseData getBankDetail(@PathVariable("batch_group_id") int batchGroupId){
        BankInfo bankInfo = prepaidCardSwitchingService.getBankInfo(batchGroupId);
        return new ResponseData()
                .setCode(ProcessMessageEnum.SUCCESS.code)
                .setStatus(ProcessMessageEnum.SUCCESS.name())
                .setMessage(ProcessMessageEnum.SUCCESS.message)
                .setData(bankInfo);
    }

    @GetMapping("/logo-prepaid/all")
    public ResponseData getLogoPrepaid() {
        String bData = prepaidCardSwitchingService.getAllLogoPrepaid();
        return new ResponseData()
                .setCode(dk.apps.pcps.model.enums.ProcessMessageEnum.SUCCESS.code)
                .setStatus(dk.apps.pcps.model.enums.ProcessMessageEnum.SUCCESS.name())
                .setMessage(dk.apps.pcps.model.enums.ProcessMessageEnum.SUCCESS.message)
                .setData(bData);
    }
}
