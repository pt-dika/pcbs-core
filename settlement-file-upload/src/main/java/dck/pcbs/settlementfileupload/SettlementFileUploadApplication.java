package dck.pcbs.settlementfileupload;

import dck.pcbs.commons.helper.file.*;
import dck.pcbs.settlement.service.SettlementModuleService;
import dck.pcbs.settlement.service.SettlementModuleServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@Slf4j
@EnableAsync
@SpringBootApplication
@EnableScheduling
@EntityScan("dck.pcbs.cds.entity")
@EnableJpaRepositories(basePackages = "dck.pcbs.cds.repository")
public class SettlementFileUploadApplication implements CommandLineRunner {

    @Value("${scheduler.cron}")
    String schedulerCron;
    @Value("${generate.bni}")
    Boolean isGenerateBni;
    @Value("${generate.mandiri}")
    Boolean isGenerateMandiri;
    @Value("${generate.bri}")
    Boolean isGenerateBri;
    @Value("${generate.bca}")
    Boolean isGenerateBca;
    @Value("${generate.dki}")
    Boolean isGenerateDki;

    public static void main(String[] args) {
        SpringApplication.run(SettlementFileUploadApplication.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("Upload Scheduled At : {}", schedulerCron);
        log.info("isGenerateBni: {}", isGenerateBni);
        log.info("isGenerateMandiri: {}", isGenerateMandiri);
        log.info("isGenerateBri: {}", isGenerateBri);
        log.info("isGenerateBca: {}", isGenerateBca);
        log.info("isGenerateDki: {}", isGenerateDki);
    }

//    @Bean(name = "uploadFSExecutor")
//    public TaskExecutor asyncExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(10);
//        executor.setMaxPoolSize(10);
//        executor.setQueueCapacity(100);
//        executor.setThreadNamePrefix("uploadFileSettlement-");
//        executor.initialize();
//        return executor;
//    }

    @Bean
    public FileService fileService(){
        return new FileServiceImpl();
    }

    @Bean
    public FileTransferService fileTransferService(){
        return new FileTransferServiceImpl();
    }

    @Bean
    public SettlementModuleService settlementModuleService(){
        return new SettlementModuleServiceImpl();
    }

}
