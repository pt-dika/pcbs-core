package dk.apps.pcps.main.controller;

import dk.apps.pcps.dbmaster.entity.MobileAppUserSession;
import dk.apps.pcps.dbmaster.service.MobileAppUsersService;
import dk.apps.pcps.dbmaster.service.MobileAppUsersSessionService;
import dk.apps.pcps.model.ResponseData;
import dk.apps.pcps.main.model.enums.ProcessMessageEnum;
import dk.apps.pcps.model.payload.ReqToken;
import dk.apps.pcps.model.payload.RespToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/auth")
public class GenerateTokenCtl {

    MobileAppUsersService mobileAppUsersService;
    MobileAppUsersSessionService mobileAppUsersSessionService;

    @Autowired
    public GenerateTokenCtl(MobileAppUsersService mobileAppUsersService,
                            MobileAppUsersSessionService mobileAppUsersSessionService){
        this.mobileAppUsersService = mobileAppUsersService;
        this.mobileAppUsersSessionService = mobileAppUsersSessionService;
    }

    @PostMapping(path = "/get_token")
    public ResponseData getTokenSession(@RequestBody ReqToken request) {
        MobileAppUserSession mobileAppUserSession = mobileAppUsersSessionService.getSession(request.getUsername(), "swipepay");
        return new ResponseData()
                .setCode(ProcessMessageEnum.SUCCESS.code)
                .setStatus(ProcessMessageEnum.SUCCESS.name())
                .setMessage(ProcessMessageEnum.SUCCESS.message)
                .setData(new RespToken().setAccessToken(mobileAppUserSession.getToken()));
    }
}
