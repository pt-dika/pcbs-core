package dk.apps.pcps.config.auth;

import dk.apps.pcps.config.PCPSConfig;
import dk.apps.pcps.dbmaster.entity.MobileAppUsers;
import dk.apps.pcps.dbmaster.service.MobileAppUsersService;
import dk.apps.pcps.main.handler.UnauthorizedException;
import dk.apps.pcps.main.utils.Utility;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import dk.apps.pcps.dbmaster.service.MobileAppUsersSessionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class ApiInterceptor implements HandlerInterceptor, AuthService {

    MobileAppUsersService mobileAppUsersService;
    MobileAppUsersSessionService mobileAppUsersSessionService;
    MobileAppUsers mobileAppUsers;

    @Autowired
    public ApiInterceptor(MobileAppUsersService mobileAppUsersService, MobileAppUsersSessionService mobileAppUsersSessionService){
        this.mobileAppUsersService = mobileAppUsersService;
        this.mobileAppUsersSessionService = mobileAppUsersSessionService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String servletPath = request.getServletPath();
        String[] allowedPath = new String[]{
                PCPSConfig.API_PATH +"/auth",
                PCPSConfig.API_PATH +"/cms"
        };
        List<String> paths = Arrays.asList(allowedPath);
        for (String path : paths){
            boolean isMatch = servletPath.contains(path);
            if (isMatch)
                return true;
        }
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty())
            throw new UnauthorizedException("authorization required");
        JSONObject jo = Utility.jwtExtractor(token);
        String username = Utility.getUserEdc(jo);
        Long expired = Utility.getUserExpiredEdc(jo);
        Long timestamp = System.currentTimeMillis() / 1000;
        if (jo == null || !mobileAppUsersSessionService.tokenExists(username, token, "swipepay"))
            throw new UnauthorizedException("invalid token");
        if (timestamp > expired )
            throw new UnauthorizedException("expired token");
        MobileAppUsers users = mobileAppUsersService.getUser(username);
//        MobileAppUsers users = new MobileAppUsers();
//        users.setUsername("megaloman");
//        Merchant merchant = new Merchant();
//        merchant.setId(8);
//        users.setMerchant(merchant);
        this.mobileAppUsers = users;
        return true;
    }

    @Override
    public MobileAppUsers getAuthUsers() {
        return this.mobileAppUsers;
    }
}
