package dk.apps.pcps.scheduled;

import dk.apps.pcps.main.module.tapcash.TapCashService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.FixedRateTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
public class ScheduledTasks implements SchedulingConfigurer {

    private static final String DEFAULT_CRON = "0 0/10 * * * ?";
    private String cron = DEFAULT_CRON;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    TapCashService transactionProcessService;

    @Autowired
    public ScheduledTasks(TapCashService transactionProcessService){
        this.transactionProcessService = transactionProcessService;
    }

    @Transactional
    public void doSettlement() {
        log.info("Starting Settlement at {}", dateFormat.format(new Date()));
//        try {
//            transactionProcessService.doSettlementBackground();
//        } catch(Exception e){
//            log.info("error "+e.getMessage()+" failed settlements");
//        }
    }

    @Bean
    public ThreadPoolTaskScheduler taskScheduler(){
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(5);
        taskScheduler.setThreadGroupName("merchant");
        taskScheduler.setThreadNamePrefix("settlement-thread-");
        return taskScheduler;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        FixedRateTask task = new FixedRateTask(() -> doSettlement(), 100000, 100000);
        taskRegistrar.addFixedRateTask(task);
    }
}
