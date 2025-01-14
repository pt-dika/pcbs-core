package dk.apps.pcps.dbmaster.service;

import dk.apps.pcps.dbmaster.entity.MobileAppUserSession;

public interface MobileAppUsersSessionService {
    boolean tokenExists(String username, String token, String from);
    MobileAppUserSession getSession(String user);
    MobileAppUserSession getSession(String user, String type);
}
