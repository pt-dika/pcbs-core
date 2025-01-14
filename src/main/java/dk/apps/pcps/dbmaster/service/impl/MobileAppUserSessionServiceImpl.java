package dk.apps.pcps.dbmaster.service.impl;

import dk.apps.pcps.main.handler.ApplicationException;
import dk.apps.pcps.main.model.enums.ProcessMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dk.apps.pcps.dbmaster.entity.MobileAppUserSession;
import dk.apps.pcps.dbmaster.repository.MobileAppUserSessionRepository;
import dk.apps.pcps.dbmaster.service.MobileAppUsersSessionService;

@Service
public class MobileAppUserSessionServiceImpl implements MobileAppUsersSessionService {

    MobileAppUserSessionRepository mobileAppUserSessionRepository;

    @Autowired
    public MobileAppUserSessionServiceImpl(MobileAppUserSessionRepository mobileAppUserSessionRepository){
        this.mobileAppUserSessionRepository = mobileAppUserSessionRepository;
    }

    @Override
    public boolean tokenExists(String username, String token, String from) {
        return mobileAppUserSessionRepository.existsByUsernameAndTokenAndHitFrom(username, token, from);
    }

    @Override
    public MobileAppUserSession getSession(String user) {
        return mobileAppUserSessionRepository.findByUsername(user);
                //.orElseThrow(() -> new ApplicationException(ProcessMessageEnum.NOT_FOUND));
    }

    @Override
    public MobileAppUserSession getSession(String user, String type) {
        return mobileAppUserSessionRepository.findByUsernameAndHitFrom(user, type)
            .orElseThrow(() -> new ApplicationException(ProcessMessageEnum.USER_NOT_LOGIN));
    }
}
