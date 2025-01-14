package dk.apps.pcps.dbmaster.service.impl;

import dk.apps.pcps.main.model.enums.ProcessMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import dk.apps.pcps.dbmaster.entity.MobileAppUsers;
import dk.apps.pcps.dbmaster.repository.MobileAppUsersRepository;
import dk.apps.pcps.dbmaster.service.MobileAppUsersService;
import dk.apps.pcps.main.handler.ApplicationException;

@Component
public class MobileAppUsersImpl implements MobileAppUsersService {

    MobileAppUsersRepository mobileAppUsersRepository;
    @Autowired
    private MobileAppUsersImpl(MobileAppUsersRepository mobileAppUsersRepository){
        this.mobileAppUsersRepository = mobileAppUsersRepository;
    }


    @Override
    public MobileAppUsers getUser(String username) {
        MobileAppUsers mobileAppUsers = mobileAppUsersRepository.findByUsername(username)
                .orElseThrow(() -> new ApplicationException(ProcessMessageEnum.NOT_FOUND,"user "+ username));
        return mobileAppUsers;
    }

    @Override
    public MobileAppUsers userExists(String username, String password) {
        //return mobileAppUsersRepository.findByUsername();
        return null;
    }

    @Override
    public int getStan(String username) {
        MobileAppUsers mobileAppUsers = getUser(username);
        int stan = mobileAppUsers.getStan() + 1;
        if (stan > 999999) {
            stan = 1;
        }
        mobileAppUsers = updateStan(username, stan);
        return mobileAppUsers.getStan();
    }

    @Override
    public MobileAppUsers updateStan(String username, int stan) {
        MobileAppUsers mobileAppUsers = getUser(username);
        mobileAppUsers.setStan(stan);
        return mobileAppUsersRepository.save(mobileAppUsers);
    }
}
