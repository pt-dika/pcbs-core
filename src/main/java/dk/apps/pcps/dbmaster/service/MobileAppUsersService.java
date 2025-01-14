package dk.apps.pcps.dbmaster.service;

import dk.apps.pcps.dbmaster.entity.MobileAppUsers;

public interface MobileAppUsersService {
    MobileAppUsers getUser(String username);
    MobileAppUsers userExists(String username, String password);
    int getStan(String username);
    MobileAppUsers updateStan(String username, int stan);
}
