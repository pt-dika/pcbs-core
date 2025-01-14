package dk.apps.pcps.config.auth;

import dk.apps.pcps.dbmaster.entity.MobileAppUsers;

public interface AuthService {
    MobileAppUsers getAuthUsers();
}
