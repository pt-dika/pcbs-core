package dk.apps.pcps.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class ApiConfiguration implements WebMvcConfigurer {

    ApiInterceptor apiInterceptor;

    @Autowired
    public ApiConfiguration(ApiInterceptor apiInterceptor){
        this.apiInterceptor = apiInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiInterceptor);
    }
}
