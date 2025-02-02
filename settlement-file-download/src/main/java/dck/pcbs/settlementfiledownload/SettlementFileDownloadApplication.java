package dck.pcbs.settlementfiledownload;

import dck.pcbs.commons.helper.file.*;
import dck.pcbs.settlement.model.SettlementResponse;
import dck.pcbs.settlement.module.EMoney;
import dck.pcbs.settlement.module.Flazz;
import dck.pcbs.settlement.module.TapCash;
import dck.pcbs.settlement.service.SettlementModuleService;
import dck.pcbs.settlement.service.SettlementModuleServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@SpringBootApplication
@EnableFeignClients
@EnableScheduling
@EntityScan("dck.pcbs.cds.entity")
@EnableJpaRepositories(basePackages = "dck.pcbs.cds.repository")
public class SettlementFileDownloadApplication implements CommandLineRunner {

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
        SpringApplication.run(SettlementFileDownloadApplication.class, args);
    }

    @Bean
    public FileService fileService(){
        return new FileServiceImpl();
    }

    @Override
    public void run(String... args) {
        log.info("Download Scheduled At: {}", schedulerCron);
        log.info("isGenerateBni: {}", isGenerateBni);
        log.info("isGenerateMandiri: {}", isGenerateMandiri);
        log.info("isGenerateBri: {}", isGenerateBri);
        log.info("isGenerateBca: {}", isGenerateBca);
        log.info("isGenerateDki: {}", isGenerateDki);
//        String homeDir = "/settlement-files";
//        String path = System.getProperty("user.dir") + homeDir;
//        String[] fsName = getBCARFSName(path, "241228");
//        System.out.println(Arrays.asList(fsName));
//        String[] fsName = getMandiriRFSName(path, "010524");
//        System.out.println(Arrays.asList(fsName));
//        String txtResponse = fileService().readTxtFile(path+"/download/BNI/", "0001004100003184103180120241224110002.RECON");
//        SettlementResponse bniSettlementResponse = TapCash.getResponse(txtResponse);
//        System.out.println(bniSettlementResponse);

    }

    private String[] getBCARFSName(String path, String rfsDate) {
        String fileName = "REPORT_SETTLEMENT_SIKLUS_08_12_"+rfsDate+"120725.TXT";
        try {
            List<Map> rfs = Flazz.readRFS(path+"/download/BCA/"+fileName+"/");
            rfs = rfs.stream().distinct().filter(distinctByKey(map -> map.get("settlement_file"))).collect(Collectors.toList());
            String[] rfsName = new String[rfs.size()];
            int i = 0;
            for (Map mData : rfs) {
                String fsName = mData.get("settlement_file").toString();
                rfsName[i] = fsName;
                i++;
            }
            return rfsName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String[] getMandiriRFSName(String path, String rfsDate) {
        String fileName = "UPP_DKI_Jakarta_"+rfsDate+"_ok.txt";
        try {
            List<Map> rfs = EMoney.readRFS(path+"/download/MANDIRI/"+fileName+"/");
            rfs = rfs.stream().distinct().filter(distinctByKey(map -> map.get("settlement_file"))).collect(Collectors.toList());
            String[] rfsName = new String[rfs.size()];
            int i = 0;
            for (Map mData : rfs) {
                String fsName = mData.get("settlement_file").toString();
                rfsName[i] = fsName;
                i++;
            }
            return rfsName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor)
    {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
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
