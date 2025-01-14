package dk.apps.pcps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
//@EnableScheduling
public class  PcpsAppsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PcpsAppsApplication.class, args);
    }

    @Bean
    public String getCronValue(){
        return "0 0/10 * * * ?";
    }
}