package com.example.quartz.jobs.init;

import com.example.quartz.tasks.SongTextShow;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobConfiguration {

    @Bean
    public Scheduler scheduler() throws SchedulerException {
         Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
         scheduler.start();
         return scheduler;
    }

//    @Bean
//    @RefreshScope
//    public GenerateEventsTask generateEvents(){
//        GenerateEventsTask generateEventsTask = new GenerateEventsTask();
//        generateEventsTask.setApw(lppEngineProperties.getApw());
//        generateEventsTask.setHotelId(lppEngineProperties.getHotelId());
//        generateEventsTask.setLos(lppEngineProperties.getLos());
//        return generateEventsTask;
//    }
//
//    @Bean
////    @RefreshScope
//    public JobDetail singJob() throws SchedulerException {
//        JobKey jobKey = new JobKey("singJob","singJobGroup");
//        JobDataMap jobDataMap = new JobDataMap();
//        jobDataMap.put("songTextShow",songTextShow);
//        jobDataMap.put("configurationHelper",configurationHelper);
//        jobDataMap.put("singer",configurationHelper.getSinger());
//        jobDataMap.put("song",configurationHelper.getSong());
//        jobDataMap.put("song",lppEngineProperties.getSong());
//        jobDataMap.put("generateEventsTask",generateEventsTask());
////        jobDataMap.put("song",environment.getProperty("song"));
//        JobDetail singJob = JobBuilder.newJob(PushHotelJob.class)
//                .withIdentity(jobKey)
//                .withDescription("the job for sing a song period")
//                .setJobData(jobDataMap)
//                .storeDurably()
//                .build();
//        TriggerKey triggerKey = new TriggerKey("singTrigger","singTriggerGroup");
//        Trigger trigger = TriggerBuilder.newTrigger()
//                .withIdentity(triggerKey)
//                .startNow()
//                .withDescription("trigger singer sing a song")
//                .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
//                .build();
//        scheduler().scheduleJob(singJob,trigger);
//        return singJob;
//    }
}
