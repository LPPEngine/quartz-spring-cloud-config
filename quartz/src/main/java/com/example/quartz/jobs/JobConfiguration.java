package com.example.quartz.jobs;

import com.example.quartz.config.manager.ConfigurationHelper;
import com.example.quartz.config.manager.LPPEngineJson;
import com.example.quartz.config.manager.LPPEngineProperties;
import com.example.quartz.service.GenerateEvents;
import com.example.quartz.service.SongTextShow;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class JobConfiguration {

    @Autowired
    private SongTextShow songTextShow;
    @Autowired
    private ConfigurationHelper configurationHelper;
    @Autowired
    private LPPEngineProperties lppEngineProperties;
    @Autowired
    private Environment environment;
    @Autowired
    private LPPEngineJson lppEngineJson;
    @Bean
    public Scheduler scheduler() throws SchedulerException {
         Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
         scheduler.start();
         return scheduler;
    }

    @Bean
    @RefreshScope
    public GenerateEvents generateEvents(){
        GenerateEvents generateEvents = new GenerateEvents();
        generateEvents.setApw(lppEngineProperties.getApw());
        generateEvents.setHotelId(lppEngineProperties.getHotelId());
        generateEvents.setLos(lppEngineProperties.getLos());
        return generateEvents;
    }

    @Bean
//    @RefreshScope
    public JobDetail singJob() throws SchedulerException {
        JobKey jobKey = new JobKey("singJob","singJobGroup");
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("songTextShow",songTextShow);
        jobDataMap.put("configurationHelper",configurationHelper);
        jobDataMap.put("singer",configurationHelper.getSinger());
//        jobDataMap.put("song",configurationHelper.getSong());
        jobDataMap.put("song",lppEngineProperties.getSong());
        jobDataMap.put("generateEvents",generateEvents());
//        jobDataMap.put("song",environment.getProperty("song"));
        JobDetail singJob = JobBuilder.newJob(Sing.class)
                .withIdentity(jobKey)
                .withDescription("the job for sing a song period")
                .setJobData(jobDataMap)
                .storeDurably()
                .build();
        TriggerKey triggerKey = new TriggerKey("singTrigger","singTriggerGroup");
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .startNow()
                .withDescription("trigger singer sing a song")
                .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
                .build();
        scheduler().scheduleJob(singJob,trigger);
        return singJob;
    }
}
