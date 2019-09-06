package com.example.quartz.jobs.init;

import com.example.quartz.configuration.manager.ConfigurationHelper;
import com.example.quartz.configuration.manager.JobConfigurationHelper;
import com.example.quartz.configuration.manager.JobConfigurationMapper;
import com.example.quartz.configuration.manager.LPPEngineProperties;
import com.example.quartz.jobs.entity.Sing;
import com.example.quartz.jobs.manage.IJobManage;
import com.example.quartz.tasks.GenerateEvents;
import com.example.quartz.tasks.SongTextShow;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:v-ksong@expedia.com">ksong</a>
 */
@Component
public class InitialJobs {

    @Autowired
    Scheduler scheduler;

    @Autowired
    IJobManage jobManage;

    @Autowired
    SongTextShow songTextShow;
    @Autowired
    ConfigurationHelper configurationHelper;
    @Autowired
    LPPEngineProperties lppEngineProperties;
    @Autowired
    GenerateEvents generateEvents;
    @Autowired
    JobConfigurationHelper jobConfigurationHelper;

    public void initialAllJobs(Map map) throws SchedulerException {
        if(!CollectionUtils.isEmpty(map)){
            JobKey jobKey = new JobKey(map.get("jobName").toString());
            if(scheduler.checkExists(jobKey)){
                return;
            }
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("songTextShow",songTextShow);
            jobDataMap.put("configurationHelper",configurationHelper);
            jobDataMap.put("singer",configurationHelper.getSinger());
//        jobDataMap.put("song",configurationHelper.getSong());
            jobDataMap.put("song",lppEngineProperties.getSong());
            jobDataMap.put("generateEvents",generateEvents);
//        jobDataMap.put("song",environment.getProperty("song"));
            jobDataMap.put("jobConfigurationHelper",jobConfigurationHelper);
            JobDetail singJob = JobBuilder.newJob(Sing.class)
                    .withIdentity(jobKey)
                    .withDescription(map.get("jobDescrition").toString())
                    .setJobData(jobDataMap)
                    .storeDurably()
                    .build();
            TriggerKey triggerKey = new TriggerKey(map.get("triggerName").toString());
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey)
                    .startNow()
                    .withDescription(map.get("triggerDescrition").toString())
                    .withSchedule(CronScheduleBuilder.cronSchedule(map.get("period").toString()))
                    .build();
            scheduler.scheduleJob(singJob,trigger);
        }
//        jobManage.add(allPropertiesHelper.getAllProperties());
    }

    public void initialAllJobs(List<JobConfigurationMapper> jobConfigurationList) throws SchedulerException {
        // initial jobs through configurations from config server
        for (JobConfigurationMapper jobConfigurationMapper: jobConfigurationList
             ) {
            JobKey jobKey = new JobKey(jobConfigurationMapper.getJobName(),jobConfigurationMapper.getJobGroup());
            if(scheduler.checkExists(jobKey)){
                continue;
            }
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("songTextShow",songTextShow);
            jobDataMap.put("configurationHelper",configurationHelper);
            jobDataMap.put("singer",configurationHelper.getSinger());
            jobDataMap.put("song",lppEngineProperties.getSong());
            jobDataMap.put("generateEvents",generateEvents);
            jobDataMap.put("jobConfigurationHelper",jobConfigurationHelper);
            JobDetail singJob = JobBuilder.newJob(Sing.class)
                    .withIdentity(jobKey)
                    .withDescription("this is a job that sing a song!")
                    .setJobData(jobDataMap)
                    .storeDurably()
                    .build();
            TriggerKey triggerKey = new TriggerKey("job1Trigger");
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey)
                    .startNow()
                    .withDescription("job test trigger!")
                    .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
                    .build();
            scheduler.scheduleJob(singJob,trigger);
        }
//        if(!CollectionUtils.isEmpty(map)){
//            JobKey jobKey = new JobKey(map.get("jobName").toString());
//            if(scheduler.checkExists(jobKey)){
//                return;
//            }
//            JobDataMap jobDataMap = new JobDataMap();
//            jobDataMap.put("songTextShow",songTextShow);
//            jobDataMap.put("configurationHelper",configurationHelper);
//            jobDataMap.put("singer",configurationHelper.getSinger());
////        jobDataMap.put("song",configurationHelper.getSong());
//            jobDataMap.put("song",lppEngineProperties.getSong());
//            jobDataMap.put("generateEvents",generateEvents);
////        jobDataMap.put("song",environment.getProperty("song"));
//            jobDataMap.put("jobConfigurationHelper",jobConfigurationHelper);
//            JobDetail singJob = JobBuilder.newJob(Sing.class)
//                    .withIdentity(jobKey)
//                    .withDescription(map.get("jobDescrition").toString())
//                    .setJobData(jobDataMap)
//                    .storeDurably()
//                    .build();
//            TriggerKey triggerKey = new TriggerKey(map.get("triggerName").toString());
//            Trigger trigger = TriggerBuilder.newTrigger()
//                    .withIdentity(triggerKey)
//                    .startNow()
//                    .withDescription(map.get("triggerDescrition").toString())
//                    .withSchedule(CronScheduleBuilder.cronSchedule(map.get("period").toString()))
//                    .build();
//            scheduler.scheduleJob(singJob,trigger);
//        }
//        jobManage.add(allPropertiesHelper.getAllProperties());
    }


}
